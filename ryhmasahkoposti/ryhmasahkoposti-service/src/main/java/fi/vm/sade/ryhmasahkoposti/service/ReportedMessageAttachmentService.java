package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageAttachment;

/**
 * Rajapinta raportoitavan ryhmäsähköpostiviestin liittyvien liitteiden käsittelyä varten
 *  
 * @author vehei1
 *
 */
public interface ReportedMessageAttachmentService {

	/**
	 * Hakee raportoitavan ryhmäsähköpostiviestiin liittyvät liitteet
	 * 
	 * @param messageID Raportoitavan ryhmäsähköpostin avain
	 * @return Lista ryhmäsähköpostiin liitettyjen liitteiden avaimia
	 */
	public List<ReportedMessageAttachment> getReportedMessageAttachments(Long messageID);
	
	/**
	 * Tallentaa raportoitavan ryhmäsähköpostiviestiin liittyvät liitteet
	 * 
	 * @param reportedMessage Raportoitavan ryhmäsähköpostiviestin tiedot
	 * @param reportedAttachments Lista ryhmäsähköpostiin liitettyjen liitteiden tietoja
	 */
	public void saveReportedMessageAttachments(ReportedMessage reportedMessage,
		List<ReportedAttachment> reportedAttachments);
}
