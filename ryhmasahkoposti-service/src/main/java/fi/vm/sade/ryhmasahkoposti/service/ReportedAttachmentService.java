package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;
import java.util.Set;

import fi.vm.sade.ryhmasahkoposti.api.dto.AttachmentResponse;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;

public interface ReportedAttachmentService {
	
	/**
	 * Hakee yksittäisen raportoitavan liitteen
	 * 
	 * @param attachmentID Liitteen tunniste
	 * @return Tunnistetta vastaava raportoitava liite
	 */
	ReportedAttachment getReportedAttachment(Long attachmentID);
	
	/**
	 * Hakee raportoitavat liitteet aiemmin tallennetujen liitteiden tietojen perusteella
	 * 
	 * @param attachmentResponses Kokoelma lähetettävien liitteiden tietoja
	 * @return Kokoelma raportoitavia liitteitä
	 */
	List<ReportedAttachment> getReportedAttachments(List<AttachmentResponse> attachmentResponses);

	/**
	 * Hakee raportoitavat liitteet aiemmin tallennetujen liitteiden tietojen perusteella
	 * 
	 * @param reportedMessageAttachments Kokoelma lähetettävien viestin liitteiden avaintietoja
	 * @return Kokoelma raportoitavia liitteitä
	 */
	List<ReportedAttachment> getReportedAttachments(Set<ReportedMessageAttachment> reportedMessageAttachments);

	/**
	 * Tallentaa ryhmäsähköpostin raportoitavat liitteet
	 * 
	 * @param reportedAttachment Raportoitavan liitteen tiedot {@link ReportedAttachment}
	 * @return Liitteen generoitu avain
	 */
	Long saveReportedAttachment(ReportedAttachment reportedAttachment);
}
