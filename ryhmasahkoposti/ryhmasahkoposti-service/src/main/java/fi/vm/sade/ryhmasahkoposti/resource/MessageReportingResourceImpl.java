package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.MessageReportingResource;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
public class MessageReportingResourceImpl implements MessageReportingResource {
	private GroupEmailReportingService groupEmailReportingService;

	@Autowired
	public MessageReportingResourceImpl(GroupEmailReportingService groupEmailReportingService) {
		this.groupEmailReportingService = groupEmailReportingService;
	}

	@Override
	public List<ReportedMessageDTO> getReportedMessages() {
		return groupEmailReportingService.getReportedMessages();
	}

	@Override
	public List<ReportedMessageDTO> getReportedMessages(String searchArgument) {
		return groupEmailReportingService.getReportedMessages(searchArgument);
	}

	@Override
	public ReportedMessageDTO getReportedMessage(Long messageID) {
		return groupEmailReportingService.getReportedMessage(messageID);
	}
}
