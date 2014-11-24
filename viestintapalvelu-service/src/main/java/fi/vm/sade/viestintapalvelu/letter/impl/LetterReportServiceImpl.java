package fi.vm.sade.viestintapalvelu.letter.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.viestintapalvelu.LetterZipUtil;
import fi.vm.sade.viestintapalvelu.dao.IPostiDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiversDAO;
import fi.vm.sade.viestintapalvelu.dto.OrganizationDTO;
import fi.vm.sade.viestintapalvelu.dto.PagingAndSortingDTO;
import fi.vm.sade.viestintapalvelu.dto.iposti.IPostiDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchReportDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterBatchesReportDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterReceiverDTO;
import fi.vm.sade.viestintapalvelu.dto.letter.LetterReceiverLetterDTO;
import fi.vm.sade.viestintapalvelu.dto.query.LetterReportQueryDTO;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.HenkiloComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.component.OrganizationComponent;
import fi.vm.sade.viestintapalvelu.externalinterface.organisaatio.OrganisaatioService;
import fi.vm.sade.viestintapalvelu.letter.LetterReportService;
import fi.vm.sade.viestintapalvelu.model.IPosti;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

@Transactional(readOnly=true)
@Service
public class LetterReportServiceImpl implements LetterReportService {
    private LetterBatchDAO letterBatchDAO;
    private LetterReceiversDAO letterReceiversDAO;
    private LetterReceiverLetterDAO letterReceiverLetterDAO;
    private IPostiDAO iPostiDAO;
    private TemplateService templateService;
    private CurrentUserComponent currentUserComponent;
    private OrganizationComponent organizationComponent;
    private HenkiloComponent henkiloComponent;
    private OrganisaatioService organisaatioService;

    @Value("${viestintapalvelu.rekisterinpitajaOID}")
    private String rekisterinpitajaOID;

    @Autowired
    public LetterReportServiceImpl(LetterBatchDAO letterBatchDAO, LetterReceiversDAO letterReceiversDAO, 
            LetterReceiverLetterDAO letterReceiverLetterDAO, IPostiDAO iPostiDAO, TemplateService templateService,
            CurrentUserComponent currentUserComponent, OrganizationComponent organizationComponent, HenkiloComponent henkiloComponent,
            OrganisaatioService organisaatioService) {
        this.letterBatchDAO = letterBatchDAO;
        this.letterReceiversDAO = letterReceiversDAO;
        this.letterReceiverLetterDAO = letterReceiverLetterDAO;
        this.iPostiDAO = iPostiDAO;
        this.templateService = templateService;
        this.currentUserComponent = currentUserComponent;
        this.organizationComponent = organizationComponent;
        this.henkiloComponent = henkiloComponent;
        this.organisaatioService = organisaatioService;
    }
    
    @Override
    public LetterBatchReportDTO getLetterBatchReport(Long letterBatchID, PagingAndSortingDTO pagingAndSorting) {               
        List<LetterReceivers> letterReceiverList = letterReceiversDAO.findLetterReceiversByLetterBatchID(
            letterBatchID, pagingAndSorting);
        
        Long numberOfReceivers = letterReceiversDAO.findNumberOfReciversByLetterBatchID(letterBatchID); 
        
        LetterReceivers letterReceivers = letterReceiverList.get(0);
        LetterBatch letterBatch = letterReceivers.getLetterBatch();
        
        LetterBatchReportDTO letterBatchReport = convertLetterBatchReport(letterBatch);
        
        List<LetterReceiverDTO> letterReceiverDTOs = 
            convertLetterReceiver(letterReceivers.getLetterBatch(), letterReceiverList);
        letterBatchReport.setLetterReceivers(letterReceiverDTOs);
        letterBatchReport.setNumberOfReceivers(numberOfReceivers);
        
        List<IPosti> iPostis = iPostiDAO.findMailById(letterBatchID);
        List<IPostiDTO> iPostiDTOs = getListOfIPostiDTO(iPostis);
        letterBatchReport.setiPostis(iPostiDTOs);
        
        if (letterBatch.getStoringOid() != null) {
            Henkilo henkilo = henkiloComponent.getHenkilo(letterBatch.getStoringOid());
            letterBatchReport.setCreatorName(henkilo.getSukunimi() + ", " + henkilo.getEtunimet());
        } else {
            letterBatchReport.setCreatorName("" + ", " + "");
        }
        return letterBatchReport;
    }

    @Override
    public LetterBatchesReportDTO getLetterBatchesReport(LetterReportQueryDTO query, PagingAndSortingDTO pagingAndSorting) {
        List<LetterBatchReportDTO> letterBatches = letterBatchDAO.findLetterBatchesBySearchArgument(query, pagingAndSorting);

        LetterBatchesReportDTO letterBatchesReport = new LetterBatchesReportDTO();
        letterBatchesReport.setLetterBatchReports(letterBatches);
        letterBatchesReport.setNumberOfLetterBatches(letterBatchDAO.findNumberOfLetterBatchesBySearchArgument(query));
        
        return letterBatchesReport;
    }

    @Override
    public LetterBatchesReportDTO getLetterBatchesReport(String organizationOID, PagingAndSortingDTO pagingAndSorting) {

        final List<LetterBatch> letterBatches;
        final long numberOfLetterBatches;
        if(organizationOID != null && organizationOID.equals(rekisterinpitajaOID)) {
            letterBatches = letterBatchDAO.findAll(pagingAndSorting);
            numberOfLetterBatches = letterBatchDAO.findNumberOfLetterBatches();
        } else {
            List<String> oids = organisaatioService.findHierarchyOids(organizationOID);
            letterBatches = letterBatchDAO.findLetterBatchesByOrganizationOid(oids, pagingAndSorting);
            numberOfLetterBatches = letterBatchDAO.findNumberOfLetterBatches(oids);
        }

        LetterBatchesReportDTO letterBatchesReport = convertLetterBatchesReport(letterBatches);
        letterBatchesReport.setNumberOfLetterBatches(numberOfLetterBatches);
        return letterBatchesReport;
    }
    
    @Override
    public LetterReceiverLetterDTO getLetterReceiverLetter(Long id) throws IOException, DataFormatException {
        LetterReceiverLetterDTO letterReceiverLetterDTO = new LetterReceiverLetterDTO();
        
        LetterReceiverLetter letterReceiverLetter = letterReceiverLetterDAO.read(id);
        LetterBatch letterBatch = letterReceiverLetter.getLetterReceivers().getLetterBatch();
        
        letterReceiverLetterDTO.setContentType(letterReceiverLetter.getOriginalContentType());
        letterReceiverLetterDTO.setId(letterReceiverLetter.getId());
        
        if (letterReceiverLetter.getContentType().equalsIgnoreCase("application/zip")) {
            letterReceiverLetterDTO.setLetter(LetterZipUtil.unZip(letterReceiverLetter.getLetter()));
        } else {
            letterReceiverLetterDTO.setLetter(letterReceiverLetter.getLetter());
        }
        
        letterReceiverLetterDTO.setTemplateName(letterBatch.getTemplateName());
        
        return letterReceiverLetterDTO;
    }


    @Override
    public List<OrganizationDTO> getUserOrganizations() {
        List<OrganizationDTO> organizations = new ArrayList<OrganizationDTO>();
        List<OrganisaatioHenkilo> organisaatioHenkiloList = currentUserComponent.getCurrentUserOrganizations();

        for (OrganisaatioHenkilo organisaatioHenkilo : organisaatioHenkiloList) {
            if (organisaatioHenkilo.isPassivoitu()) {
                continue;
            }
            OrganizationDTO organization = new OrganizationDTO();

            OrganisaatioRDTO organisaatioRDTO = organizationComponent.getOrganization(organisaatioHenkilo
                    .getOrganisaatioOid());
            String organizationName = organizationComponent.getNameOfOrganisation(organisaatioRDTO);

            organization.setOid(organisaatioRDTO.getOid());
            organization.setName(organizationName);

            organizations.add(organization);
        }

        return organizations;
    }

    private List<IPostiDTO> getListOfIPostiDTO(List<IPosti> iPostis) {
        List<IPostiDTO> iPostiDTOList = new ArrayList<IPostiDTO>();
        
        for (IPosti iPosti : iPostis) {
            IPostiDTO iPostiDTO = new IPostiDTO();
            iPostiDTO.setContent(iPosti.getContent());
            iPostiDTO.setContentName(iPosti.getContentName());
            iPostiDTO.setContentType(iPosti.getContentType());
            iPostiDTO.setId(iPosti.getId());
            iPostiDTO.setSentDate(iPosti.getSentDate());
            
            iPostiDTOList.add(iPostiDTO);
        }
        
        return iPostiDTOList;
    }

    private LetterBatchesReportDTO convertLetterBatchesReport(List<LetterBatch> letterBatches) {
        LetterBatchesReportDTO letterBatchesReport = new LetterBatchesReportDTO();
        
        List<LetterBatchReportDTO> letterBatchReports = new ArrayList<LetterBatchReportDTO>();
        
        for (LetterBatch letterBatch : letterBatches) {
            LetterBatchReportDTO letterBatchReport = convertLetterBatchReport(letterBatch);
            letterBatchReports.add(letterBatchReport);
        }

        letterBatchesReport.setLetterBatchReports(letterBatchReports);
        
        return letterBatchesReport;
    }

    private LetterBatchReportDTO convertLetterBatchReport(LetterBatch letterBatch) {
        LetterBatchReportDTO letterBatchReport = new LetterBatchReportDTO();

        letterBatchReport.setTemplateName(letterBatch.getTemplateName());
        letterBatchReport.setApplicationPeriod(letterBatch.getApplicationPeriod());
        letterBatchReport.setDeliveryTypeIPosti(letterBatch.isIposti());
        letterBatchReport.setFetchTarget(letterBatch.getFetchTarget());
        letterBatchReport.setLetterBatchID(letterBatch.getId());
        letterBatchReport.setTag(letterBatch.getTag());
        letterBatchReport.setTimestamp(letterBatch.getTimestamp());
        letterBatchReport.setOrganisaatioOid(letterBatch.getOrganizationOid());

        Template template = templateService.findByIdAndState(letterBatch.getTemplateId(), ContentStructureType.letter, null);
        letterBatchReport.setTemplate(template);
        if (template != null) {
            letterBatchReport.setTemplateName(template.getName());
        }
        if (letterBatch.getBatchStatus() != null) {
            letterBatchReport.setStatus(letterBatch.getBatchStatus().name());
        }
        return letterBatchReport;
    }

    private List<LetterReceiverDTO> convertLetterReceiver(LetterBatch letterBatch, List<LetterReceivers> letterReceiverList) {
        List<LetterReceiverDTO> letterReceiverDTOs = new ArrayList<LetterReceiverDTO>();

        for (LetterReceivers letterReceivers : letterReceiverList) {
            LetterReceiverDTO letterReceiverDTO = new LetterReceiverDTO();
            
            letterReceiverDTO.setAddress1(letterReceivers.getLetterReceiverAddress().getAddressline());
            letterReceiverDTO.setAddress2(letterReceivers.getLetterReceiverAddress().getAddressline2());
            letterReceiverDTO.setCity(letterReceivers.getLetterReceiverAddress().getCity());
            letterReceiverDTO.setContentType(letterReceivers.getLetterReceiverLetter().getContentType());
            letterReceiverDTO.setCountry(letterReceivers.getLetterReceiverAddress().getCountry());
            letterReceiverDTO.setId(letterReceivers.getId());
            letterReceiverDTO.setLetterReceiverLetterID(letterReceivers.getLetterReceiverLetter().getId());
            letterReceiverDTO.setName(letterReceivers.getLetterReceiverAddress().getLastName() + ", " + 
                letterReceivers.getLetterReceiverAddress().getFirstName());
            letterReceiverDTO.setPostalCode(letterReceivers.getLetterReceiverAddress().getPostalCode());
            letterReceiverDTO.setRegion(letterReceivers.getLetterReceiverAddress().getRegion());
            letterReceiverDTO.setTemplateName(letterBatch.getTemplateName());
            
            letterReceiverDTOs.add(letterReceiverDTO);
        }
        return letterReceiverDTOs;
    }

}
