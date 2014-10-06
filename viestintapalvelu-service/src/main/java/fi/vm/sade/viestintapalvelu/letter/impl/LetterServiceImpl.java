package fi.vm.sade.viestintapalvelu.letter.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.ws.rs.NotFoundException;

import org.apache.pdfbox.util.PDFMergerUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.dao.*;
import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.LetterBuilder;
import fi.vm.sade.viestintapalvelu.letter.LetterService;
import fi.vm.sade.viestintapalvelu.letter.dto.*;
import fi.vm.sade.viestintapalvelu.letter.dto.converter.LetterBatchDtoConverter;
import fi.vm.sade.viestintapalvelu.letter.processing.IPostiProcessable;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.util.CollectionHelper;

/**
 * @author migar1
 * 
 */
@Service
@Transactional
public class LetterServiceImpl implements LetterService {
    private static final int MAX_IPOST_CHUNK_SIZE = 500;
    public static final String APPLICATION_ZIP = "application/zip";

    private static final Logger logger = LoggerFactory.getLogger(LetterServiceImpl.class);


    private LetterBatchDAO letterBatchDAO;
    private LetterReceiverLetterDAO letterReceiverLetterDAO;
    private LetterReceiversDAO letterReceiversDAO;
    private CurrentUserComponent currentUserComponent;
    private TemplateDAO templateDAO;
    private LetterBatchDtoConverter letterBatchDtoConverter;
    private IPostiDAO iPostiDAO;
    private ObjectMapperProvider objectMapperProvider;
    // Causes circular reference if autowired directly, through applicationContext laxily:
    private LetterBuilder letterBuilder;
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    public LetterServiceImpl(LetterBatchDAO letterBatchDAO, LetterReceiverLetterDAO letterReceiverLetterDAO,
            CurrentUserComponent currentUserComponent, TemplateDAO templateDAO,
            LetterBatchDtoConverter letterBatchDtoConverter,
            LetterReceiversDAO letterReceiversDAO,
            ObjectMapperProvider objectMapperProvider,
            IPostiDAO iPostiDAO) {
    	this.letterBatchDAO = letterBatchDAO;
    	this.currentUserComponent = currentUserComponent;
        this.templateDAO = templateDAO;
        this.letterReceiversDAO = letterReceiversDAO;
        this.letterReceiverLetterDAO = letterReceiverLetterDAO;
        this.letterBatchDtoConverter = letterBatchDtoConverter;
        this.objectMapperProvider = objectMapperProvider;
        this.iPostiDAO = iPostiDAO;
    }

    /* ---------------------- */
    /* - Create LetterBatch - */
    /* ---------------------- */
    @Override
    @Transactional
    public LetterBatch createLetter(AsyncLetterBatchDto letterBatch) {
        // kirjeet.kirjelahetys
        LetterBatch model = new LetterBatch();
        letterBatchDtoConverter.convert(letterBatch, model);
        model.setTimestamp(new Date());
        model.setStoringOid(getCurrentHenkilo().getOidHenkilo());
        model.setBatchStatus(LetterBatch.Status.created);

        // kirjeet.vastaanottaja
        ObjectMapper mapper = objectMapperProvider.getContext(getClass());
        try {
            model.setLetterReceivers(parseLetterReceiversModels(letterBatch, model, mapper));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON parsing of letter receiver replacement failed: " + e.getMessage(), e);
        }
        model.setUsedTemplates(parseUsedTemplates(letterBatch, model));

        addIpostData(letterBatch, model);
        return storeLetterBatch(model);
    }

    /* ---------------------- */
    /* - Create LetterBatch - */
    /* ---------------------- */
    @Override
    @Transactional
    public LetterBatch createLetter(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch) {
        // kirjeet.kirjelahetys
        LetterBatch model = new LetterBatch();
        letterBatchDtoConverter.convert(letterBatch, model);
        model.setTimestamp(new Date());
        model.setBatchStatus(LetterBatch.Status.created);
        model.setStoringOid(getCurrentHenkilo().getOidHenkilo());

        // kirjeet.vastaanottaja
        ObjectMapper mapper = objectMapperProvider.getContext(getClass());
        try {
            model.setLetterReceivers(parseLetterReceiversModels(letterBatch, model, mapper));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("JSON parsing of letter receiver replacement failed: " + e.getMessage(), e);
        }
        model.setUsedTemplates(parseUsedTemplates(letterBatch, model));

        addIpostData(letterBatch, model);
        return storeLetterBatch(model);
    }

    private Henkilo getCurrentHenkilo() {
        logger.debug("getting current user!!! ");
        Henkilo henkilo = currentUserComponent.getCurrentUser();
        logger.debug("getting current user!!!  got " + henkilo);
        return henkilo;
    }


    private void addIpostData(LetterBatchDetails letterBatch, LetterBatch model) {
        Map<String, byte[]> ipostiData = letterBatch.getIPostiData();
        if (ipostiData != null) {
            for(Map.Entry<String, byte[]> data: ipostiData.entrySet()){
                model.addIPosti(createIPosti(model, data));
            }
        }
    }

    private Set<UsedTemplate> parseUsedTemplates(LetterBatchDetails letterBatch, LetterBatch letterB) {
        Set<UsedTemplate> templates = new HashSet<UsedTemplate>();
        Set<String> languageCodes = new HashSet<String>();
        String templateName = getTemplateNameFromBatch(letterBatch);
        for (LetterDetails letter : letterBatch.getLetters()) {
            languageCodes.add(letter.getLanguageCode());
        }
        languageCodes.add(letterBatch.getLanguageCode());
        for (String languageCode : languageCodes) {
            fi.vm.sade.viestintapalvelu.model.Template template = templateDAO.findTemplateByName(templateName, languageCode);
            if (template != null) {
                UsedTemplate usedTemplate = new UsedTemplate();
                usedTemplate.setLetterBatch(letterB);
                usedTemplate.setTemplate(template);
                templates.add(usedTemplate);
            }
        }
        return templates;
    }

    private String getTemplateNameFromBatch(LetterBatchDetails letterBatch) {
        fi.vm.sade.viestintapalvelu.model.Template template = templateDAO.findTemplateByName(letterBatch.getTemplateName(), letterBatch.getLanguageCode());
        return template != null ? template.getName() : templateDAO.read(letterBatch.getTemplateId()).getName();
    }

    /* ------------ */
    /* - findById - */
    /* ------------ */
    @Transactional(readOnly = true)
    public fi.vm.sade.viestintapalvelu.letter.LetterBatch findById(long id) {
        LetterBatch searchResult = null;
        List<LetterBatch> letterBatch = letterBatchDAO.findBy("id", id);
        if (letterBatch != null && !letterBatch.isEmpty()) {
            searchResult = letterBatch.get(0);
        } else {
        	return null;
        }

        // kirjeet.kirjelahetys
        fi.vm.sade.viestintapalvelu.letter.LetterBatch result = new fi.vm.sade.viestintapalvelu.letter.LetterBatch();
        result.setTemplateId(searchResult.getTemplateId());
        result.setTemplateName(searchResult.getTemplateName());
        result.setApplicationPeriod(searchResult.getApplicationPeriod());
        result.setFetchTarget(searchResult.getFetchTarget());
        result.setLanguageCode(searchResult.getLanguage());
        result.setStoringOid(searchResult.getStoringOid());
        result.setOrganizationOid(searchResult.getOrganizationOid());
        result.setTag(searchResult.getTag());
        // kirjeet.lahetyskorvauskentat
        result.setTemplateReplacements(parseReplDTOs(searchResult.getLetterReplacements()));

        // kirjeet.vastaanottaja
        // result.setLetters(parseLetterDTOs(searchResult.getLetterReceivers()));
        // Not implemented

        return result;
    }

    /* ------------------------------- */
    /* - findLetterBatchByNameOrgTag - */
    /* ------------------------------- */
    @Transactional(readOnly = true)
    public fi.vm.sade.viestintapalvelu.letter.LetterBatch findLetterBatchByNameOrgTag(String templateName,
                          String languageCode, String organizationOid,
                          Optional<String> tag, Optional<String> applicationPeriod) {
        fi.vm.sade.viestintapalvelu.letter.LetterBatch result = new fi.vm.sade.viestintapalvelu.letter.LetterBatch();

        LetterBatch letterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(templateName, languageCode,
            organizationOid, tag, applicationPeriod);
        if (letterBatch != null) {

            // kirjeet.kirjelahetys
            result.setTemplateId(letterBatch.getTemplateId());
            result.setTemplateName(letterBatch.getTemplateName());
            result.setFetchTarget(letterBatch.getFetchTarget());
            result.setApplicationPeriod(letterBatch.getApplicationPeriod());
            result.setTag(letterBatch.getTag());
            result.setLanguageCode(letterBatch.getLanguage());
            result.setStoringOid(letterBatch.getStoringOid());
            result.setOrganizationOid(letterBatch.getOrganizationOid());
            result.setTag(letterBatch.getTag());
            // kirjeet.lahetyskorvauskentat
            result.setTemplateReplacements(parseReplDTOs(letterBatch.getLetterReplacements()));
        }

        return result;
    }

    /* ------------------------------- */
    /* - findReplacementByNameOrgTag - */
    /* ------------------------------- */
    @Transactional(readOnly = true)
    public List<fi.vm.sade.viestintapalvelu.template.Replacement> findReplacementByNameOrgTag(String templateName,
                          String languageCode, String organizationOid,
                          Optional<String> tag, Optional<String> applicationPeriod) {

        List<fi.vm.sade.viestintapalvelu.template.Replacement> replacements = new LinkedList<fi.vm.sade.viestintapalvelu.template.Replacement>();
        LetterBatch letterBatch = null;
        if (!tag.isPresent() && !applicationPeriod.isPresent()) {
            letterBatch = letterBatchDAO.findLetterBatchByNameOrg(templateName, languageCode, organizationOid);
        } else {
            letterBatch = letterBatchDAO.findLetterBatchByNameOrgTag(templateName, languageCode, organizationOid, tag,
                    applicationPeriod);
        }
        if (letterBatch != null) {

            // kirjeet.lahetyskorvauskentat
            for (LetterReplacement letterRepl : letterBatch.getLetterReplacements()) {
                fi.vm.sade.viestintapalvelu.template.Replacement repl = new fi.vm.sade.viestintapalvelu.template.Replacement();
                repl.setId(letterRepl.getId());
                repl.setName(letterRepl.getName());
                repl.setDefaultValue(letterRepl.getDefaultValue());
                repl.setMandatory(letterRepl.isMandatory());
                repl.setTimestamp(letterRepl.getTimestamp());

                replacements.add(repl);
            }
        }
        return replacements;
    }

    /* ------------- */
    /* - getLetter - */
    /* ------------- */
    @Transactional(readOnly = true)
    public fi.vm.sade.viestintapalvelu.letter.LetterContent getLetter(long id) {

        List<LetterReceiverLetter> letterReceiverLetter = letterReceiverLetterDAO.findBy("id", id);

        fi.vm.sade.viestintapalvelu.letter.LetterContent content = new fi.vm.sade.viestintapalvelu.letter.LetterContent();

        if (letterReceiverLetter != null && !letterReceiverLetter.isEmpty()) {
            LetterReceiverLetter lb = letterReceiverLetter.get(0);

            content.setContentType(lb.getOriginalContentType());
            content.setTimestamp(lb.getTimestamp());

            if (APPLICATION_ZIP.equals(lb.getContentType())) {
                try {
                    content.setContent(unZip(lb.getLetter()));

                } catch (IOException e) {
                    content.setContent(lb.getLetter());
                    content.setContentType(lb.getContentType());
                } catch (DataFormatException e) {
                    content.setContent(lb.getLetter());
                    content.setContentType(lb.getContentType());
                }
            } else {
                content.setContent(lb.getLetter());
            }

        }

        return content;
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getLetterContentsByLetterBatchID(Long letterBatchID) throws Exception {
        ByteArrayOutputStream contentsOutputStream = new ByteArrayOutputStream();
        PDFMergerUtility merger = new PDFMergerUtility();
        
        LetterBatch letterBatch = letterBatchDAO.read(letterBatchID);
        
        Set<LetterReceivers> letterReceivers = letterBatch.getLetterReceivers();
        for (LetterReceivers letterReceiver : letterReceivers) {
            LetterReceiverLetter letter = letterReceiver.getLetterReceiverLetter();

            byte[] content = letter.getLetter();
            if (letter.getContentType().equals(APPLICATION_ZIP)) {
                content = unZip(content);
            }
            merger.addSource(new ByteArrayInputStream(content));
        }
        
        merger.setDestinationStream(contentsOutputStream);
        merger.mergeDocuments();
        
        return contentsOutputStream.toByteArray();
    }
    
    private IPosti createIPosti(LetterBatch letterB, Map.Entry<String, byte[]> data) {
        return createIPosti(letterB, data.getKey(), data.getValue());
    }
    
    private IPosti createIPosti(LetterBatch letterB, String name, byte[] data) {
        IPosti iPosti = new IPosti();
        iPosti.setLetterBatch(letterB);
        iPosti.setContent(data);
        iPosti.setContentName(name);
        iPosti.setContentType(APPLICATION_ZIP);
        iPosti.setCreateDate(new Date());
        return iPosti;
    }
    /*
     * kirjeet.vastaanottaja
     */
    private Set<LetterReceivers> parseLetterReceiversModels(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch,
                                LetterBatch letterB, ObjectMapper mapper) throws JsonProcessingException {
        Set<LetterReceivers> receivers = new HashSet<LetterReceivers>();
        for (fi.vm.sade.viestintapalvelu.letter.Letter letter : letterBatch.getLetters()) {
            fi.vm.sade.viestintapalvelu.model.LetterReceivers rec = letterBatchDtoConverter.
                    convert(letter, new fi.vm.sade.viestintapalvelu.model.LetterReceivers(), mapper);
            rec.setLetterBatch(letterB);

            // kirjeet.vastaanottajakirje
            if (letter.getLetterContent() != null) {
                LetterReceiverLetter lrl = new LetterReceiverLetter();
                lrl.setTimestamp(new Date());

                boolean zippaa = true;

                if (zippaa) { // ZIP
                    try {
                        lrl.setLetter(zip(letter.getLetterContent().getContent()));
                        lrl.setContentType(APPLICATION_ZIP); // application/zip
                        lrl.setOriginalContentType(letter.getLetterContent().getContentType()); // application/pdf

                    } catch (IOException e) {
                        lrl.setLetter(letter.getLetterContent().getContent());
                        lrl.setContentType(letter.getLetterContent().getContentType()); // application/pdf
                        lrl.setOriginalContentType(letter.getLetterContent().getContentType()); // application/pdf
                    }

                } else { // Not zipped
                    lrl.setLetter(letter.getLetterContent().getContent());
                    lrl.setContentType(letter.getLetterContent().getContentType()); // application/pdf
                    lrl.setOriginalContentType(letter.getLetterContent().getContentType()); // application/pdf
                }

                lrl.setLetterReceivers(rec);

                rec.setLetterReceiverLetter(lrl);
            }

            receivers.add(rec);
        }
        return receivers;
    }

    private Set<LetterReceivers> parseLetterReceiversModels(AsyncLetterBatchDto letterBatch, LetterBatch letterB,
                                                            ObjectMapper mapper) throws JsonProcessingException {
        Set<LetterReceivers> receivers = new HashSet<LetterReceivers>();
        for (AsyncLetterBatchLetterDto letter : letterBatch.getLetters()) {
            fi.vm.sade.viestintapalvelu.model.LetterReceivers rec = letterBatchDtoConverter.
                    convert(letter, new fi.vm.sade.viestintapalvelu.model.LetterReceivers(), mapper);
            rec.setLetterBatch(letterB);

            // kirjeet.vastaanottajakirje, luodaan aina tyhjänä:
            LetterReceiverLetter lrl = new LetterReceiverLetter();
            lrl.setTimestamp(new Date());
            lrl.setLetterReceivers(rec);
            rec.setLetterReceiverLetter(lrl);

            receivers.add(rec);
        }
        return receivers;
    }

    /*
     * kirjeet.lahetyskorvauskentat
     */
    private Set<LetterReplacement> parseLetterReplacementsModels(fi.vm.sade.viestintapalvelu.letter.LetterBatch letterBatch, LetterBatch letterB) {
        Set<LetterReplacement> replacements = new HashSet<LetterReplacement>();

        Object replKeys[] = letterBatch.getTemplateReplacements().keySet().toArray();
        Object replVals[] = letterBatch.getTemplateReplacements().values().toArray();

        for (int i = 0; i < replVals.length; i++) {
            fi.vm.sade.viestintapalvelu.model.LetterReplacement repl = new fi.vm.sade.viestintapalvelu.model.LetterReplacement();

            repl.setName(replKeys[i].toString());
            repl.setDefaultValue(replVals[i].toString());
            // repl.setMandatory();
            // TODO: tähän tietyt kentät Mandatory true esim. title body ...

            repl.setTimestamp(new Date());
            repl.setLetterBatch(letterB);
            replacements.add(repl);
        }
        return replacements;
    }

    private Map<String, Object> parseReplDTOs(Set<LetterReplacement> letterReplacements) {
        Map<String, Object> replacements = new HashMap<String, Object>();

        for (LetterReplacement letterRepl : letterReplacements) {
            replacements.put(letterRepl.getName(), letterRepl.getDefaultValue());
        }
        return replacements;
    }

    private LetterBatch storeLetterBatch(LetterBatch letterB) {
         return letterBatchDAO.insert(letterB);
    }
    
    private static byte[] unZip(byte[] content) throws IOException, DataFormatException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(content.length);
    
        Inflater inflater = new Inflater();
        inflater.setInput(content);
    
        byte[] buffer = new byte[1024];
    
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
    
        outputStream.close();
        return outputStream.toByteArray();
    }

    private static byte[] zip(byte[] content) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(content.length);
    
        Deflater deflater = new Deflater();
        deflater.setInput(content);
        deflater.finish();
    
        byte[] buffer = new byte[1024];
    
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
        return outputStream.toByteArray();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateBatchProcessingStarted(long id, LetterBatchProcess process) {
        LetterBatch batch = letterBatchDAO.read(id);
        if (batch == null) {
            throw new NotFoundException("LetterBatch not found by id="+id);
        }
        switch (process) {
        case EMAIL:
            if (batch.getEmailHandlingStarted() != null) {
                throw new IllegalStateException("Email handling already stated at "
                        +batch.getEmailHandlingStarted()+" for LetterBatch="+batch.getTemplateId());
            }
            batch.setEmailHandlingStarted(new Date());
            break;
        case LETTER:
            batch.setHandlingStarted(new Date());
            batch.setBatchStatus(LetterBatch.Status.processing);
            break;
        case IPOSTI:
            batch.setIpostHandlingFinished(new Date());
            batch.setBatchStatus(LetterBatch.Status.processing_ipost);
            break;
        }
        letterBatchDAO.update(batch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findUnprocessedLetterReceiverIdsByBatch(long batchId) {
        return letterBatchDAO.findUnprocessedLetterReceiverIdsByBatch(batchId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> findAllReceiversIdsByBatch(long batchId) {
        return letterBatchDAO.findAllLetterReceiverIdsByBatch(batchId);
    }

    @Override
    @Transactional
    public void saveBatchErrorForReceiver(Long letterReceiverId, String message) {
        LetterReceivers receiver = letterReceiversDAO.read(letterReceiverId);

        LetterBatch batch = receiver.getLetterBatch();
        batch.setBatchStatus(LetterBatch.Status.error);

        List<LetterBatchProcessingError> errors = new ArrayList<LetterBatchProcessingError>();
        LetterBatchLetterProcessingError error = new LetterBatchLetterProcessingError();
        error.setErrorTime(new Date());
        error.setLetterReceivers(receiver);
        error.setLetterBatch(batch);
        error.setErrorCause(Optional.fromNullable(message).or("Unknown (TODO)"));
        errors.add(error);
        batch.setProcessingErrors(errors);
        letterBatchDAO.update(batch);
    }

    @Override
    @Transactional
    public void processLetterReceiver(long receiverId) throws Exception {
        LetterReceivers receiver = letterReceiversDAO.read(receiverId);
        LetterBatch batch = receiver.getLetterBatch();
        ObjectMapper mapper = objectMapperProvider.getContext(getClass());
        getLetterBuilder().constructPDFForLetterReceiverLetter(receiver, batch, formReplacementMap(batch),
                formReplacementMap(receiver, mapper));
        letterReceiverLetterDAO.update(receiver.getLetterReceiverLetter());
    }

    protected void processIpostiBatchForLetterReceivers(long letterBatchId,
                            List<Long> batchReceiversIds, int index) throws Exception {
        logger.info("Processing IPosti {} for LetterBatch={} with {} receivers", index, letterBatchId,
                batchReceiversIds.size());
        LetterBatch batch = letterBatchDAO.read(letterBatchId);
        List<LetterReceiverLetter> receiverLetters =
                letterReceiverLetterDAO.getLetterReceiverLettersByLetterReceiverIds(batchReceiversIds);
        String templateName = batch.getTemplateName();
        String lang = batch.getLanguage();
        String zipName = templateName + "_" + lang + "_" + index + ".zip";
        byte[] iPostZipBytes = getLetterBuilder().printZIP(receiverLetters, templateName, zipName);
        IPosti iposti = createIPosti(batch, zipName, iPostZipBytes);
        iPostiDAO.insert(iposti);

        for (LetterReceiverLetter letter : receiverLetters) {
            LetterReceivers receiver = letter.getLetterReceivers();
            receiver.setContainedInIposti(iposti);
            letterReceiversDAO.update(receiver);
        }
    }

    private Map<String, Object> formReplacementMap(fi.vm.sade.viestintapalvelu.model.LetterBatch batch) {
        Map<String, Object> replacements = new HashMap<String, Object>();
        for (LetterReplacement repl : batch.getLetterReplacements()) {
            replacements.put(repl.getName(), repl.getDefaultValue());
        }
        return replacements;
    }

    private Map<String, Object> formReplacementMap(LetterReceivers receiver, ObjectMapper mapper) throws IOException {
        Map<String, Object> replacements = new HashMap<String, Object>();
        for (LetterReceiverReplacement repl : receiver.getLetterReceiverReplacement()) {
            replacements.put(repl.getName(), repl.getEffectiveValue(mapper));
        }
        return replacements;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Optional<LetterBatchProcess> updateBatchProcessingFinished(long id, LetterBatchProcess process) {
        LetterBatchProcess nextProcess = null;
        LetterBatch batch = letterBatchDAO.read(id);
        if (batch == null) {
            throw new NotFoundException("LetterBatch not found by id="+id);
        }
        switch (process) {
        case EMAIL:
            logger.info("EMAIL processing finished for  letter batch {}", id);
            batch.setEmailHandlingFinished(new Date());
            break;
        case LETTER:
            batch.setHandlingFinished(new Date());
            if (batch.isIposti()) {
                logger.info("LETTER processing finished for IPosti letter batch {}", id);
                batch.setBatchStatus(LetterBatch.Status.waiting_for_ipost_processing);
                nextProcess = LetterBatchProcess.IPOSTI;
            } else {
                logger.info("LETTER processing finished for  letter batch {}", id);
                batch.setBatchStatus(LetterBatch.Status.ready);
            }
            break;
        case IPOSTI:
            logger.info("IPOSTI processing finished for  letter batch {}", id);
            batch.setIpostHandlingFinished(new Date());
            batch.setBatchStatus(LetterBatch.Status.ready);
            break;
        }
        letterBatchDAO.update(batch);
        if (nextProcess != null) {
            logger.info("Current status {}, next process: {}", batch.getBatchStatus(), nextProcess);
        }
        return Optional.fromNullable(nextProcess);
    }

    @Override
    @Transactional(readOnly = true)
    public LetterBatch fetchById(long id) {
        return letterBatchDAO.read(id);
    }

    @Override
    @Transactional(readOnly = true)
    public LetterBatchStatusDto getBatchStatus(long batchId) {
        LetterBatchStatusDto batch = letterBatchDAO.getLetterBatchStatus(batchId);

        if(batch == null) {
            batch = new LetterBatchStatusDto(null, null, null, LetterBatch.Status.error, 0);
            List<LetterBatchProcessingError> errors = new ArrayList<LetterBatchProcessingError>();
            LetterBatchProcessingError error = new LetterBatchLetterProcessingError();
            error.setErrorCause("Batch not found for id " + batchId);
            error.setErrorTime(new Date());
            errors.add(error);
            batch.setErrors(errors);
            return batch;
        }

        //This happens when an earlier batch process has encountered an error when processing the batch
        //with the given batchId.
        //The error messages are stored in the actual model class, so we need to fetch that
        //to get hold of the error messages and pass them to the DTO.
        if(LetterBatch.Status.error.equals(batch.getStatus())) {
            LetterBatch actualBatch = fetchById(batchId);
            List<LetterBatchProcessingError> processingErrors = actualBatch.getProcessingErrors();
            actualBatch.getLetterReceivers();
            batch.setErrors(processingErrors);
            return batch;
        }

        return batch;
    }

    @Override
    @Transactional
    public void updateLetter(LetterReceiverLetter letter) {
        letterReceiverLetterDAO.update(letter);
    }

    @Override
    public List<Long> findUnfinishedLetterBatches() {
        return letterBatchDAO.findUnfinishedLetterBatches();
    }

    @Override
    @Transactional
    public LetterBatchSplitedIpostDto splitBatchForIpostProcessing(long letterBatchId) {
        logger.info("splitBatchForIpostProcessing {}", letterBatchId);
        LetterBatch batch = letterBatchDAO.read(letterBatchId);

        LetterBatchSplitedIpostDto job = new LetterBatchSplitedIpostDto();
        List<Long> allReceiverIds = letterReceiversDAO.findLetterRecieverIdsByLetterBatchId(letterBatchId);
        List<List<Long>> splitted = CollectionHelper.split(allReceiverIds, MAX_IPOST_CHUNK_SIZE);
        int orderNumber = 1;
        for (List<Long> receiverIds : splitted) {
            logger.info("Total {} receivers in letterBatch's {} IPosti {}", receiverIds.size(), letterBatchId, orderNumber);
            IPostiProcessable processable = new IPostiProcessable(letterBatchId, orderNumber);
            processable.addLetterReceiverIds(receiverIds);
            job.add(processable);
            orderNumber++;
        }
        return job;
    }

    @Override
    @Transactional
    public void processIposti(IPostiProcessable processable) {
        try {
            processIpostiBatchForLetterReceivers(
                    processable.getLetterBatchId(),
                    processable.getLetterReceiverIds(), processable.getOrderNumber());
        } catch (Exception e) {
            logger.error("iposti processing failed ", e);
        }
    }

    @Override
    @Transactional
    public void handleIpostError(IPostiProcessable iPostiProcessable, Exception e) {
        logger.error("Error processing IPostiProcessable: "+iPostiProcessable+". Message: "+e.getMessage(), e);

        LetterBatch batch = letterBatchDAO.read(iPostiProcessable.getLetterBatchId());
        batch.setBatchStatus(LetterBatch.Status.error);

        LetterBatchIPostProcessingError error = new LetterBatchIPostProcessingError();
        error.setOrderNumber(iPostiProcessable.getOrderNumber());
        error.setLetterBatch(batch);
        error.setErrorTime(new Date());
        error.setErrorCause(Optional.fromNullable(e.getMessage()).or("Unknown"));
        batch.getProcessingErrors().add(error);
        letterBatchDAO.update(batch);
    }

    public void setLetterBuilder(LetterBuilder letterBuilder) {
        this.letterBuilder = letterBuilder;
    }

    public LetterBuilder getLetterBuilder() {
        if (this.letterBuilder == null && this.applicationContext != null) {
            this.letterBuilder = applicationContext.getBean(LetterBuilder.class);
        }
        return this.letterBuilder;
    }

    public void setLetterBatchDAO(LetterBatchDAO letterBatchDAO) {
        this.letterBatchDAO = letterBatchDAO;
    }

    public void setLetterReceiverLetterDAO(LetterReceiverLetterDAO letterReceiverLetterDAO) {
        this.letterReceiverLetterDAO = letterReceiverLetterDAO;
    }

    public void setCurrentUserComponent(CurrentUserComponent currentUserComponent) {
        this.currentUserComponent = currentUserComponent;
    }

    public void setLetterReceiversDAO(LetterReceiversDAO letterReceiversDAO) {
        this.letterReceiversDAO = letterReceiversDAO;
    }

    public void setObjectMapperProvider(ObjectMapperProvider objectMapperProvider) {
        this.objectMapperProvider = objectMapperProvider;
    }

}
