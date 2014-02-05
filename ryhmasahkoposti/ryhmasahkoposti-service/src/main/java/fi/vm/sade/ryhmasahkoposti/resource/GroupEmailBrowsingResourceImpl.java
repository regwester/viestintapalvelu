package fi.vm.sade.ryhmasahkoposti.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.resource.GroupEmailBrowsingResource;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Component
public class GroupEmailBrowsingResourceImpl implements GroupEmailBrowsingResource {
	private GroupEmailReportingService groupEmailReportingService;

	@Autowired
	public GroupEmailBrowsingResourceImpl(GroupEmailReportingService groupEmailReportingService) {
		this.groupEmailReportingService = groupEmailReportingService;
	}

	@Override
	public List<ReportedMessageDTO> getBrowsingMessages() {
		System.out.println("getRaportoitavatViestit()");
		return null;
	}

	@Override
	public List<ReportedMessageDTO> getBrowsingMessages(String searchArgument) {
		return groupEmailReportingService.getReportedMessages(searchArgument);
	}
}
