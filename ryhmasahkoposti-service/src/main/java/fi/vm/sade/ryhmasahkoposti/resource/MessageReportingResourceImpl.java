package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
public class MessageReportingResourceImpl implements MessageReportingResource {
    private static Logger logger = LoggerFactory.getLogger(MessageReportingResourceImpl.class);
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
                                        String order) throws Exception {
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

        return Response.ok(reportedMessagesDTO).build();
    }

    @Override
    public Response getReportedMessages(String organizationOid, String searchArgument, Integer nbrOfRows,
                                        Integer page, String sortedBy, String order) throws Exception {
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

        return Response.ok(reportedMessagesDTO).build();
    }

    @Override
    public Response getReportedMessagesSentByCurrentUser(String process) throws Exception {
        String senderOid = groupEmailReportingService.getCurrentUserOid();

        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(null, null);
        ReportedMessagesDTO reportedMessagesDTO = groupEmailReportingService.getReportedMessagesBySenderOid(senderOid, pagingAndSorting);

        return Response.ok(reportedMessagesDTO).build();

    }

    @Override
    public Response getReportedMessage(Long messageID) throws Exception {
        ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessage(messageID);
        return Response.ok(reportedMessageDTO).build();
    }

    @Override
    public Response getReportedMessageAndRecipients(Long messageID, Integer nbrOfRows, Integer page,
                                                    String sortedBy, String order) throws Exception {
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
        ReportedMessageDTO reportedMessageDTO = groupEmailReportingService.getReportedMessageAndRecipients(
                messageID, pagingAndSorting);
        return Response.ok(reportedMessageDTO).build();
    }

    @Override
    public Response getReportedMessageAndRecipientsSendingUnsuccessful(Long messageID, Integer nbrOfRows,
                                                                       Integer page, String sortedBy, String order) throws Exception {
        PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
        ReportedMessageDTO reportedMessageDTO =
                groupEmailReportingService.getReportedMessageAndRecipientsSendingUnsuccessful(messageID, pagingAndSorting);
        return Response.ok(reportedMessageDTO).build();
    }

    @Override
    public Response downloadReportedMessageAttachment(Long attachmentID) throws Exception {
        ReportedAttachment reportedAttachment = groupEmailReportingService.getAttachment(attachmentID);
        byte[] binary = reportedAttachment.getAttachment();
        return Response.ok(binary).header("Content-Disposition", "attachment; filename=\"" + reportedAttachment.getAttachmentName() + "\"").build();
    }
}
