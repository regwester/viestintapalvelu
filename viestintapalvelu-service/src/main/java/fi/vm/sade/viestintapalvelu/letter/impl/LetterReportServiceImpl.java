package fi.vm.sade.viestintapalvelu.letter.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import fi.vm.sade.viestintapalvelu.letter.LetterReportService;
import fi.vm.sade.viestintapalvelu.model.IPosti;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;
import fi.vm.sade.viestintapalvelu.model.LetterReceivers;
import fi.vm.sade.viestintapalvelu.template.Template;
import fi.vm.sade.viestintapalvelu.template.TemplateService;

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
    
    @Autowired
    public LetterReportServiceImpl(LetterBatchDAO letterBatchDAO, LetterReceiversDAO letterReceiversDAO, 
        LetterReceiverLetterDAO letterReceiverLetterDAO, IPostiDAO iPostiDAO, TemplateService templateService, 
        CurrentUserComponent currentUserComponent, OrganizationComponent organizationComponent, HenkiloComponent henkiloComponent) {
        this.letterBatchDAO = letterBatchDAO;
        this.letterReceiversDAO = letterReceiversDAO;
        this.letterReceiverLetterDAO = letterReceiverLetterDAO;
        this.iPostiDAO = iPostiDAO;
        this.templateService = templateService;
        this.currentUserComponent = currentUserComponent;
        this.organizationComponent = organizationComponent;
        this.henkiloComponent = henkiloComponent;
    }

    
    @Override
    public LetterBatchReportDTO getLetterBatchReport(Long letterBatchID, PagingAndSortingDTO pagingAndSorting) {               
        List<LetterReceivers> letterReceiverList = letterReceiversDAO.findLetterReceiversByLetterBatchID(
            letterBatchID, pagingAndSorting);
        
        LetterReceivers letterReceivers = letterReceiverList.get(0);
        LetterBatch letterBatch = letterReceivers.getLetterBatch();
        
        LetterBatchReportDTO letterBatchReport = getLetterBatchReport(letterBatch);
        
        List<LetterReceiverDTO> letterReceiverDTOs = 
            getLetterReceiver(letterReceivers.getLetterBatch(), letterReceiverList);        
        letterBatchReport.setLetterReceivers(letterReceiverDTOs);
        
        List<IPosti> iPostis = iPostiDAO.findMailById(letterBatchID);
        List<IPostiDTO> iPostiDTOs = getListOfIPostiDTO(iPostis);
        letterBatchReport.setiPostis(iPostiDTOs);
        
        Henkilo henkilo = henkiloComponent.getHenkilo(letterBatch.getStoringOid());
        letterBatchReport.setCreatorName(henkilo.getSukunimi() + ", " + henkilo.getEtunimet());
        
        return letterBatchReport;
    }

    @Override
    public LetterBatchesReportDTO getLetterBatchesReport(LetterReportQueryDTO query, PagingAndSortingDTO pagingAndSorting) {
        List<LetterBatch> letterBatches = letterBatchDAO.findLetterBatchesBySearchArgument(query, pagingAndSorting);
        
        LetterBatchesReportDTO letterBatchesReport = getLetterBatchesReport(letterBatches);
        letterBatchesReport.setNumberOfLetterBatches(letterBatchDAO.findNumberOfLetterBatchesBySearchArgument(query));
        
        return letterBatchesReport;
    }

    @Override
    public LetterBatchesReportDTO getLetterBatchesReport(String organizationOID, PagingAndSortingDTO pagingAndSorting) {
        List<LetterBatch> letterBatches = letterBatchDAO.findLetterBatchesByOrganizationOid(organizationOID, 
            pagingAndSorting);
        
        LetterBatchesReportDTO letterBatchesReport = getLetterBatchesReport(letterBatches);
        letterBatchesReport.setNumberOfLetterBatches(letterBatchDAO.findNumberOfLetterBatches(organizationOID));
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
    
    private LetterBatchesReportDTO getLetterBatchesReport(List<LetterBatch> letterBatches) {
        LetterBatchesReportDTO letterBatchesReport = new LetterBatchesReportDTO();
        
        List<LetterBatchReportDTO> letterBatchReports = new ArrayList<LetterBatchReportDTO>();
        
        for (LetterBatch letterBatch : letterBatches) {
            LetterBatchReportDTO letterBatchReport = getLetterBatchReport(letterBatch);            
            letterBatchReports.add(letterBatchReport);
        }

        letterBatchesReport.setLetterBatchReports(letterBatchReports);
        
        return letterBatchesReport;
    }

    private LetterBatchReportDTO getLetterBatchReport(LetterBatch letterBatch) {
        LetterBatchReportDTO letterBatchReport = new LetterBatchReportDTO();
        
        letterBatchReport.setApplicationPeriod(letterBatch.getApplicationPeriod());
        letterBatchReport.setDeliveryTypeIPosti(isLetterBatchSentByIPosti(letterBatch));
        letterBatchReport.setFetchTargetName(getFetchTargetName(letterBatch));
        letterBatchReport.setLetterBatchID(letterBatch.getId());
        letterBatchReport.setTag(letterBatch.getTag());
        letterBatchReport.setTimestamp(letterBatch.getTimestamp());
        
        Template template = templateService.findById(letterBatch.getTemplateId());
        letterBatchReport.setTemplate(template);
        
        return letterBatchReport;
    }
    
    private List<LetterReceiverDTO> getLetterReceiver(LetterBatch letterBatch, List<LetterReceivers> letterReceiverList) {
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

    private String getFetchTargetName(LetterBatch letterBatch) {
        if (letterBatch.getFetchTarget() == null) {
            return "";
        }
        
        OrganisaatioRDTO organization = organizationComponent.getOrganization(letterBatch.getFetchTarget());
        return organizationComponent.getNameOfOrganisation(organization);
    }
    
    private boolean isLetterBatchSentByIPosti(LetterBatch letterBatch) {
        List<IPosti> iPostis = iPostiDAO.findMailById(letterBatch.getId());
        
        if (iPostis == null || iPostis.isEmpty()) {
            return false;
        }
        
        return true;
    }

}
