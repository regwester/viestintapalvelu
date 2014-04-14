package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.OrganizationDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.MessageReportingResource;
import fi.vm.sade.ryhmasahkoposti.converter.PagingAndSortingDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageQueryDTOConverter;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
public class MessageReportingResourceImpl implements MessageReportingResource {
	private GroupEmailReportingService groupEmailReportingService;
	private ReportedMessageQueryDTOConverter reportedMessageQueryDTOConverter;
	private PagingAndSortingDTOConverter pagingAndSortingDTOConverter;

	@Autowired
	public MessageReportingResourceImpl(GroupEmailReportingService groupEmailReportingService, 
	    ReportedMessageQueryDTOConverter reportedMessageQueryDTOConverter, 
	    PagingAndSortingDTOConverter pagingAndSortingDTOConverter) {
		this.groupEmailReportingService = groupEmailReportingService;
		this.reportedMessageQueryDTOConverter = reportedMessageQueryDTOConverter;
		this.pagingAndSortingDTOConverter = pagingAndSortingDTOConverter;
	}

	@Override
    public ReportedMessagesDTO getReportedMessages(String organizationOid, Integer nbrOfRows, Integer page,
        String sortedBy, String order) {
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
        List<OrganizationDTO> organizations = groupEmailReportingService.getUserOrganizations();

        if (organizationOid == null || organizationOid.isEmpty()) { 
            organizationOid = organizations.get(0).getOid();
        }

        ReportedMessagesDTO reportedMessagesDTO = 
            groupEmailReportingService.getReportedMessagesByOrganizationOid(organizationOid, pagingAndSorting);
        
        reportedMessagesDTO.setOrganizations(organizations);
        
        for (int i = 0; i < organizations.size(); i++) {
            OrganizationDTO organization = organizations.get(i);
            if (organization.getOid().equals(organizationOid)) {
                reportedMessagesDTO.setSelectedOrganization(i);
                break;
            }
        }
         
        return reportedMessagesDTO;
    }

    @Override
	public ReportedMessagesDTO getReportedMessages(String organizationOid, String searchArgument, Integer nbrOfRows, 
	    Integer page, String sortedBy, String order) {
	    PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
	    List<OrganizationDTO> organizations = groupEmailReportingService.getUserOrganizations();
	    
	    if (organizationOid == null || organizationOid.isEmpty()) { 
	        organizationOid = organizations.get(0).getOid();
	    }
	    
        ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(organizationOid, searchArgument);	    
        ReportedMessagesDTO reportedMessagesDTO = groupEmailReportingService.getReportedMessages(query, pagingAndSorting);

        reportedMessagesDTO.setOrganizations(organizations);

        for (int i = 0; i < organizations.size(); i++) {
            OrganizationDTO organization = organizations.get(i);
            if (organization.getOid().equals(organizationOid)) {
                reportedMessagesDTO.setSelectedOrganization(i);
                break;
            }
        }

		return reportedMessagesDTO;
	}

	@Override
	public ReportedMessageDTO getReportedMessage(Long messageID) {
		return groupEmailReportingService.getReportedMessage(messageID);
	}

    @Override
    public ReportedMessageDTO getReportedMessageAndRecipients(Long messageID, Integer nbrOfRows, Integer page, 
        String sortedBy, String order) {
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
        return groupEmailReportingService.getReportedMessageAndRecipients(messageID, pagingAndSorting);
    }
    
    @Override
    public ReportedMessageDTO getReportedMessageAndRecipientsSendingUnsuccesful(Long messageID, Integer nbrOfRows, 
        Integer page, String sortedBy, String order) {
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
        return groupEmailReportingService.getReportedMessageAndRecipientsSendingUnsuccesful(messageID, pagingAndSorting);
    }    
}
