package fi.vm.sade.ryhmasahkoposti.service;

import java.util.List;

import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessage;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;

/**
 * Rajapinta raportoitavan ryhmäsähköpostiviestin liittyvien liitteiden käsittelyä varten
 *  
 * @author vehei1
 *
 */
public interface ReportedMessageAttachmentService {

	/**
	 * Tallentaa raportoitavan ryhmäsähköpostiviestiin liittyvät liitteet
	 * 
	 * @param reportedMessage Raportoitavan ryhmäsähköpostiviestin tiedot
	 * @param reportedAttachments Lista ryhmäsähköpostiin liitettyjen liitteiden tietoja
	 */
	void saveReportedMessageAttachments(ReportedMessage reportedMessage,
		List<ReportedAttachment> reportedAttachments);

    /**
     * Tallentaa raportoitavan ryhmäsähköpostiviestin vastaanottajaan liittyvät liitteet
     *
     * @param emailRecipient Raportoitavan ryhmäsähköpositn vastaanottaja
     * @param reportedAttachments Lista vastaajanottajaan liitettyjen liitteiden tietoja
     */
    void saveReportedRecipientAttachments(ReportedRecipient emailRecipient, List<ReportedAttachment> reportedAttachments);

}
