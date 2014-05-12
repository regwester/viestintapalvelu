package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.RestConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.OrganizationDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.query.ReportedMessageQueryDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.MessageReportingResource;
import fi.vm.sade.ryhmasahkoposti.converter.PagingAndSortingDTOConverter;
import fi.vm.sade.ryhmasahkoposti.converter.ReportedMessageQueryDTOConverter;
import fi.vm.sade.ryhmasahkoposti.exception.ExternalInterfaceException;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
public class MessageReportingResourceImpl implements MessageReportingResource {
    private static Logger LOGGER = LoggerFactory.getLogger(MessageReportingResourceImpl.class);
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
    public Response getReportedMessages(String organizationOid, Integer nbrOfRows, Integer page, String sortedBy, 
        String order) {
        try {        
            List<OrganizationDTO> organizations = groupEmailReportingService.getUserOrganizations();

            if (organizationOid == null || organizationOid.isEmpty()) { 
                organizationOid = organizations.get(0).getOid();
            }

            PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
            ReportedMessagesDTO reportedMessagesDTO = groupEmailReportingService.getReportedMessagesByOrganizationOid(
                organizationOid, pagingAndSorting);
            
            reportedMessagesDTO.setOrganizations(organizations);
            
            for (int i = 0; i < organizations.size(); i++) {
                OrganizationDTO organization = organizations.get(i);
                if (organization.getOid().equals(organizationOid)) {
                    reportedMessagesDTO.setSelectedOrganization(i);
                    break;
                }
            }
            
            Response response = Response.ok(reportedMessagesDTO).build();
            return response;
        } catch (ExternalInterfaceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();
        }
    }

    @Override
	public Response getReportedMessages(String organizationOid, String searchArgument, Integer nbrOfRows, 
	    Integer page, String sortedBy, String order) {
	    try {
	        List<OrganizationDTO> organizations = groupEmailReportingService.getUserOrganizations();
	    
    	    if (organizationOid == null || organizationOid.isEmpty()) { 
    	        organizationOid = organizations.get(0).getOid();
    	    }
    	    
            ReportedMessageQueryDTO query = reportedMessageQueryDTOConverter.convert(organizationOid, searchArgument);	    
            PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
            ReportedMessagesDTO reportedMessagesDTO = groupEmailReportingService.getReportedMessages(query, pagingAndSorting);
    
            reportedMessagesDTO.setOrganizations(organizations);
    
            for (int i = 0; i < organizations.size(); i++) {
                OrganizationDTO organization = organizations.get(i);
                if (organization.getOid().equals(organizationOid)) {
                    reportedMessagesDTO.setSelectedOrganization(i);
                    break;
                }
            }    

            Response response = Response.ok(reportedMessagesDTO).build();
            return response;
	    } catch (ExternalInterfaceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();
        }
	}

	@Override
	public Response getReportedMessage(Long messageID) {
		try {
		    ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessage(messageID);
		    return Response.ok(reportedMessageDTO).build();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();
        }
	}

    @Override
    public Response getReportedMessageAndRecipients(Long messageID, Integer nbrOfRows, Integer page, 
        String sortedBy, String order) {
        try {
            PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
            ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessageAndRecipients(
                messageID, pagingAndSorting);
            return Response.ok(reportedMessageDTO).build();
        } catch (ExternalInterfaceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();            
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();            
        }
    }
    
    @Override
    public Response getReportedMessageAndRecipientsSendingUnsuccesful(Long messageID, Integer nbrOfRows, 
        Integer page, String sortedBy, String order) {
        try {
            PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
            ReportedMessageDTO reportedMessageDTO = 
                groupEmailReportingService.getReportedMessageAndRecipientsSendingUnsuccesful(messageID, pagingAndSorting);
            return Response.ok(reportedMessageDTO).build();
        } catch (ExternalInterfaceException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();            
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(RestConstants.INTERNAL_SERVICE_ERROR).build();            
        }
    }    
}
