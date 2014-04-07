package fi.vm.sade.ryhmasahkoposti.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.PagingAndSortingDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessagesDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.MessageReportingResource;
import fi.vm.sade.ryhmasahkoposti.converter.PagingAndSortingDTOConverter;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
public class MessageReportingResourceImpl implements MessageReportingResource {
	private GroupEmailReportingService groupEmailReportingService;
	private PagingAndSortingDTOConverter pagingAndSortingDTOConverter;

	@Autowired
	public MessageReportingResourceImpl(GroupEmailReportingService groupEmailReportingService, 
	    PagingAndSortingDTOConverter pagingAndSortingDTOConverter) {
		this.groupEmailReportingService = groupEmailReportingService;
		this.pagingAndSortingDTOConverter = pagingAndSortingDTOConverter;
	}

	@Override
	public ReportedMessagesDTO getReportedMessages(Integer nbrOfRows, Integer page, String sortedBy, String order) {
	    PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
		return groupEmailReportingService.getReportedMessages(pagingAndSorting);
	}

	@Override
	public ReportedMessagesDTO getReportedMessages(String searchArgument, Integer nbrOfRows, Integer page, 
	    String sortedBy, String order) {
	    PagingAndSortingDTO pagingAndSorting = pagingAndSortingDTOConverter.convert(nbrOfRows, page, sortedBy, order);
		return groupEmailReportingService.getReportedMessages(searchArgument, pagingAndSorting);
	}

	@Override
	public ReportedMessageDTO getReportedMessage(Long messageID) {
		return groupEmailReportingService.getReportedMessage(messageID);
	}

    @Override
    public ReportedMessageDTO getReportedMessageAndRecipients(Long messageID, Integer nbrOfRows, Integer page, String sortedBy, 
        String order) {
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
