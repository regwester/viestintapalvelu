/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.asiontitili.impl;

import java.io.IOException;
import java.util.*;

import javax.ws.rs.NotFoundException;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Supplier;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.asiontitili.AsiointitiliService;
import fi.vm.sade.viestintapalvelu.asiontitili.api.dto.*;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteria;
import fi.vm.sade.viestintapalvelu.dao.criteria.TemplateCriteriaImpl;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.AsiointitiliCommunicationService;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.*;
import fi.vm.sade.viestintapalvelu.letter.LetterBuilder;
import fi.vm.sade.viestintapalvelu.letter.html.TextWithoutHtmlCleaner;
import fi.vm.sade.viestintapalvelu.letter.html.XhtmlCleaner;
import fi.vm.sade.viestintapalvelu.template.Contents;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateContent;
import fi.vm.sade.viestintapalvelu.template.TemplateService;
import fi.vm.sade.viestintapalvelu.util.BeanValidator;

import static com.google.common.base.Optional.fromNullable;
import static fi.vm.sade.viestintapalvelu.util.HetuPrinterUtil.clearHetu;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 14:39
 */
@Service
public class AsiointitiliServiceImpl implements AsiointitiliService {
    private static final Logger logger = LoggerFactory.getLogger(AsiointitiliServiceImpl.class);

    public static final String HETU_ASIKAS_TUNNUS_TYYPP = "SSN";
    public static final String ASIOINTITILI_TEMPLATE_TYPE = "asiointitili";
    public static final String VIRANOMAISTUNNISTE_PREFIX = "oph-viestinta-";
    public static final int ASIAKASTILI_EXISTS_STATUS = 300;
    public static final int SINGLE_MESSAGE_SMS_LENGTH = 160;
    public static final String URL_CONTENT_TYPE = "text/url";
    public static final int URL_LENGTH = 10;
    public static final String APPLICATION_PDF_CONTENT_TYPE = "application/pdf";
    public static final int BYTES_IN_KILOBYTE = 1024;

    private static final Supplier<? extends AddressLabel> EMPTY_ADDRESS_LABEL = new Supplier<AddressLabel>() {
        public AddressLabel get() {
            return new AddressLabel();
        }
    };
    private static final Supplier<? extends Map<String, Object>> EMPTY_MAP = new Supplier<Map<String, Object>>() {
        public Map<String, Object> get() {
            return new HashMap<String, Object>();
        }
    };

    @Autowired
    private AsiointitiliCommunicationService asiointitiliCommunicationService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private LetterBuilder letterBuilder;

    @Autowired
    private DocumentBuilder documentBuilder;

    @Autowired
    private BeanValidator beanValidator;

    @Override
    @Transactional
    public AsiointitiliAsyncResponseDto send(AsiointitiliSendBatchDto sendBatch) throws NotFoundException {
        Template template = resolveTemplate(sendBatch);

        Map<String,AsiointitiliMessageDto> messagesByHetu = new HashMap<String, AsiointitiliMessageDto>();
        AsiakasTilaTarkastusKyselyDto kysely = buildAsiakaskysely(sendBatch, messagesByHetu);

        AsiakasTilaKyselyVastausDto tilat = asiointitiliCommunicationService.tarkistaAsiointitilinTila(kysely);

        AsiointitiliAsyncResponseDto response = new AsiointitiliAsyncResponseDto();
        response.setId(UUID.randomUUID().toString());
        List<AsiointitiliMessageDto> messagesToSend = resolveReceivers(messagesByHetu, tilat, response);

        KohdeLisaysDto batch = buildBatch(sendBatch, template, messagesToSend);
        KohdeLisaysVastausDto batchResponse = asiointitiliCommunicationService.lisaaKohteitaAsiointitilille(batch);
        response.setMessageId(batchResponse.getSanomaTunniste());
        response.setStatusCode(batchResponse.getTilaKoodi());
        response.setDescription(batchResponse.getKuvaus());
        for (KohdeJaAsiakasTilaDto batchResponseCustomerStatus : batchResponse.getKohteet()) {
            AsiointitiliReceiverStatusDto receiverStatus = new AsiointitiliReceiverStatusDto();
            AsiakasJaKohteenTilaDto customer = batchResponseCustomerStatus.getAsiakas().get(0); // should contain only one
            receiverStatus.setId(batchResponseCustomerStatus.getViranomaisTunniste());
            receiverStatus.setStateCode(customer.getKohteenTila());
            receiverStatus.setStateDescription(customer.getKohteenTilaKuvaus());
            receiverStatus.setAsiointitiliExists(isAccount(customer.getKohteenTila()));
            AsiointitiliMessageDto message = messagesByHetu.get(customer.getAsiakasTunnus());
            if (message != null) {
                receiverStatus.setReceiverOid(message.getReceiverHenkiloOid());
            }
            response.getReceiverStatuses().add(receiverStatus);
        }

        return response;
    }

    private Template resolveTemplate(AsiointitiliSendBatchDto sendBatch) throws NotFoundException {
        TemplateCriteria templateCriteria =
                new TemplateCriteriaImpl(sendBatch.getTemplateName(), sendBatch.getLanguageCode())
                        .withType(ASIOINTITILI_TEMPLATE_TYPE)
                        .withApplicationPeriod(sendBatch.getApplicationPeriod());
        Template template = templateService.getTemplateByName(templateCriteria, true);
        if (template == null) {
            throw new NotFoundException("No asiointitili template found by " + templateCriteria);
        }
        return template;
    }

    private AsiakasTilaTarkastusKyselyDto buildAsiakaskysely(AsiointitiliSendBatchDto sendBatch,
                                                             Map<String, AsiointitiliMessageDto> messagesByHetu) {
        AsiakasTilaTarkastusKyselyDto kysely = new AsiakasTilaTarkastusKyselyDto();
        for (AsiointitiliMessageDto message :  sendBatch.getMessages()) {
            AsiakasDto asiakas = new AsiakasDto();
            kysely.getAsiakkaat().add(asiakas);
            asiakas.setAsiakasTunnus(message.getReceiverHetu());
            asiakas.setTunnusTyyppi(HETU_ASIKAS_TUNNUS_TYYPP);
            messagesByHetu.put(message.getReceiverHetu(), message);
        }
        return kysely;
    }

    private List<AsiointitiliMessageDto> resolveReceivers(Map<String, AsiointitiliMessageDto> messagesByHetu, AsiakasTilaKyselyVastausDto tilat, AsiointitiliAsyncResponseDto response) {
        List<AsiointitiliMessageDto> messagesToSend = new ArrayList<AsiointitiliMessageDto>();
        for (AsiakasTilaDto asikasTila : tilat.getAsiakkaat()) {
            AsiointitiliMessageDto message = messagesByHetu.get(asikasTila.getAsiakasTunnus());
            boolean accountExists = isAccount(asikasTila.getTila());
            if (message == null) {
                // Should not happen:
                logger.error("> (message id: {}) UNKNOWN customer: HETU: {}, state: {}",
                        tilat.getSanomaTunniste(),
                        clearHetu(asikasTila.getAsiakasTunnus()),
                        asikasTila.getTila());
            } else {
                logger.info("> (message id: {}) customer OID={} state: {}",
                        tilat.getSanomaTunniste(),
                        message.getReceiverHenkiloOid(),
                        asikasTila.getTila());
                if (!accountExists) {
                    AsiointitiliReceiverStatusDto status = new AsiointitiliReceiverStatusDto();
                    status.setId(tilat.getSanomaTunniste());
                    status.setReceiverOid(message.getReceiverHenkiloOid());
                    status.setStateCode(asikasTila.getTila());
                    status.setAsiointitiliExists(false);
                    response.getReceiverStatuses().add(status);
                } else {
                    messagesToSend.add(message);
                }
            }
        }
        return messagesToSend;
    }

    private boolean isAccount(int customerState) {
        return customerState <= ASIAKASTILI_EXISTS_STATUS;
    }

    private KohdeLisaysDto buildBatch(AsiointitiliSendBatchDto sendBatch, Template template, List<AsiointitiliMessageDto> messagesToSend) {
        KohdeLisaysDto batchDto = new KohdeLisaysDto();
        Map<String,Object> templateReplacements = letterBuilder.getTemplateReplacements(template);
        for (AsiointitiliMessageDto messageDto : messagesToSend) {
            KohdeDto kohde = produceMessage(sendBatch, template, templateReplacements, messageDto);
            batchDto.getKohteet().add(kohde);
        }
        // Especially validate for lengths of fields:
        beanValidator.validate(batchDto);
        return batchDto;
    }

    private KohdeDto produceMessage(AsiointitiliSendBatchDto sendBatch,
                        Template template, Map<String, Object> templateReplacements,
                        AsiointitiliMessageDto messageDto) {
        @SuppressWarnings("unchecked") Map<String,Object> dataContext = letterBuilder.createDataContext(
                XhtmlCleaner.INSTANCE, // because may be used with attachments as well
                template,
                fromNullable(messageDto.getAddressLabel()).or(EMPTY_ADDRESS_LABEL),
                templateReplacements,
                fromNullable(sendBatch.getTemplateReplacements()).or(EMPTY_MAP),
                fromNullable(messageDto.getTemplateReplacements()).or(EMPTY_MAP)
        );
        String content = cleanHtmlToText(fromNullable(messageDto.getOverriddenContent())
                        .or(fromNullable(sendBatch.getOverriddenContent()))
                        .or(templateContent(template, Contents.ASIOINTITILI_CONTENT, dataContext))),
                smsContent = cleanHtmlToText(fromNullable(messageDto.getOverriddenSmsContent())
                        .or(fromNullable(sendBatch.getOverriddenSmsContent()))
                        .or(templateContent(template, Contents.ASIOINTITILI_SMS_CONTENT, dataContext))),
                header = cleanHtmlToText(fromNullable(messageDto.getOverriddenHeader())
                        .or(fromNullable(sendBatch.getOverriddenHeader()))
                        .or(templateContent(template, Contents.ASIOINTITILI_HEADER, dataContext)));

        KohdeDto kohde = new KohdeDto();
        kohde.setNimeke(header);
        kohde.setKuvausTeksti(content);
        if (smsContent.length() > SINGLE_MESSAGE_SMS_LENGTH) {
            throw new IllegalArgumentException("SMS content is longer than "+SINGLE_MESSAGE_SMS_LENGTH
                    + " characters ("+smsContent+"). Multiple messages are no allowed!");
        }
        if (smsContent.length() > 0) {
            kohde.setSmsLisatieto(smsContent);
        }
        kohde.setLahettajaNimi(sendBatch.getSenderName());
        kohde.setViranomaisTunniste(VIRANOMAISTUNNISTE_PREFIX + UUID.randomUUID().toString());
        AsiakasDto asiakas = new AsiakasDto();
        asiakas.setTunnusTyyppi(HETU_ASIKAS_TUNNUS_TYYPP);
        asiakas.setAsiakasTunnus(messageDto.getReceiverHetu());
        kohde.getAsiakkaat().add(asiakas);
        kohde.getTiedostot().addAll(buildAttachments(sendBatch));
        processAttachmentsByTemplateContent(template, messageDto, dataContext);
        kohde.getTiedostot().addAll(buildAttachments(messageDto));

        return kohde;
    }

    private String cleanHtmlToText(String str) {
        return TextWithoutHtmlCleaner.INSTANCE.clean(str);
    }

    private Supplier<? extends String> templateContent(final Template template, final String contentName,
                                                       final Map<String, Object> dataContext) {
        return new Supplier<String>() {
            public String get() {
                for (TemplateContent content : template.getContents()) {
                    if (contentName.equalsIgnoreCase(content.getName())) {
                        return evaluateTemplateContent(content, dataContext, template);
                    }
                }
                return "";
            }
        };
    }
    
    private String evaluateTemplateContent(TemplateContent content, Map<String, Object> dataContext,
                                           Template template) {
        String contentStr = content.getContent();
        try {
            return new String(documentBuilder
                    .applyTextTemplate(contentStr.getBytes(), dataContext));
        } catch (IOException e) {
            throw new IllegalStateException("Error processing template's ("
                    +template.getName()+":"+template.getLanguage()+") velocity content "
                    +content.getName() + ": " +e.getMessage(), e);
        }
    }

    private void processAttachmentsByTemplateContent(Template template, AsiointitiliMessageDto messageDto, Map<String, Object> dataContext) {
        for (TemplateContent attachmentContent : Contents.attachmentsFor(template.getName())
                .filter(template.getContents())) {
            String contentXhtml = evaluateTemplateContent(attachmentContent, dataContext, template);
            byte[] contentPdf;
            try {
                contentPdf = documentBuilder.xhtmlToPDF(contentXhtml.getBytes());
            } catch (Exception e) {
                throw new IllegalStateException("Error processing PDF of template's ("
                        +template.getName()+":"+template.getLanguage()+") velocity content "
                        +attachmentContent.getName() + ": " +e.getMessage(), e);
            }
            if (messageDto.getAttachments() == null) {
                messageDto.setAttachments(new ArrayList<AsiointitiliAttachmentDto>());
            }
            AsiointitiliAttachmentDto pdfAttachment = new AsiointitiliAttachmentDto();
            pdfAttachment.setName(clearAttachmentName(attachmentContent.getName()) + ".pdf");
            pdfAttachment.setDescription(attachmentContent.getName());
            pdfAttachment.setSize(contentPdf.length / BYTES_IN_KILOBYTE);
            pdfAttachment.setContent(new String(Base64.encodeBase64(contentPdf)));
            pdfAttachment.setContentType(APPLICATION_PDF_CONTENT_TYPE);
            messageDto.getAttachments().add(pdfAttachment);
        }
    }

    private String clearAttachmentName(String name) {
        return name.replaceAll(" ", "_")
                .replaceAll("[^A-Za-z0-9öäåÖÄÅ_.]", "");
    }

    private List<TiedostoDto> buildAttachments(LinkOrAttachmentContainer container) {
        List<TiedostoDto> attachments = new ArrayList<TiedostoDto>();
        if (container.getLinks() != null) {
            for (AsiointitiliLinkDto link : container.getLinks()) {
                TiedostoDto attachment = new TiedostoDto();
                attachment.setNimi(link.getName());
                attachment.setKuvaus(link.getDescription());
                attachment.setUrl(link.getUrl());
                attachment.setKoko(URL_LENGTH);
                attachment.setMuoto(URL_CONTENT_TYPE);
                attachments.add(attachment);
            }
        }
        if (container.getAttachments() != null) {
            for (AsiointitiliAttachmentDto source : container.getAttachments()) {
                TiedostoDto attachment = new TiedostoDto();
                attachment.setNimi(source.getName());
                attachment.setSisalto(source.getContent());
                attachment.setKoko(source.getSize());
                attachment.setKuvaus(source.getDescription());
                attachment.setMuoto(source.getContentType());
                attachments.add(attachment);
            }
        }
        return attachments;
    }


}
