package fi.vm.sade.ryhmasahkoposti.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessageDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipientDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailResponse;
import fi.vm.sade.ryhmasahkoposti.service.EmailService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedAttachmentService;
import fi.vm.sade.ryhmasahkoposti.service.GroupEmailReportingService;

@Service
public class EmailServiceImpl implements EmailService {
	private static final Logger log = Logger
			.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailServiceImpl.class
					.getName());

	private static final int MAX_CACHE_ENTRIES = 10;

	@Autowired
	private GroupEmailReportingService rrService;

	@Autowired
	private ReportedAttachmentService liiteService;

	@Autowired
	private EmailSender emailSender;

	@Value("${ryhmasahkoposti.queue.handle.size}")
	String queueSizeString = "1000";

	private Map<Long, EmailMessageDTO> messageCache = new LinkedHashMap<Long, EmailMessageDTO>(
			MAX_CACHE_ENTRIES + 1) {
		private static final long serialVersionUID = 1L;

		protected boolean removeEldestEntry(
				Map.Entry<Long, EmailMessageDTO> eldest) {
			return size() > MAX_CACHE_ENTRIES;
		};
	};

	@Override
	public EmailResponse sendEmail(EmailMessage email) {

		// email.setFooter(email.getHeader().getLanguageCode());
		log.info("Send email info: " + email.toString());

		boolean sendStatus = emailSender.sendMail(email,
				"tähän vastaanottajan osoite jotenkin");
		String status = (sendStatus ? "OK" : "Error");
		// email.setSendStatus(status); LAITETAAN KANTAAN.

		// MITEN TÄMÄ MUUTTUU
		// EmailResponse resp = new EmailResponse(email.getHeader(), status,
		// email.getSubject(), Integer.toString(email.getAttachments().size()));
		EmailResponse resp = new EmailResponse(status, email.getSubject());
		log.info("Email  response: " + resp.toString());
		return resp;
	}

	/**
	 * Spring scheduler method
	 */
	public void handleEmailQueue() {
		int queueMaxSize = 1000;
		try {
			queueMaxSize = Integer.parseInt(queueSizeString);
		} catch (NumberFormatException nfe) {
			// never mind use default
		}
		long start = System.currentTimeMillis();
		log.info("Handling queue (max size "+ queueMaxSize + "). Sending emails using: " +emailSender);
		
		List<EmailRecipientDTO> queue = rrService.getUnhandledMessageRecipients(queueMaxSize);
		
		int sent = 0;
		int errors = 0;
		int queueSize = queue.size();
		for (EmailRecipientDTO er : queue) {
			long vStart = System.currentTimeMillis();
			log.info("Handling " + er + " " +er.getRecipientID());
			if (rrService.startSending(er)) {
				log.info("Handling really " + er + " " +er.getRecipientID());
				Long messageId = er.getEmailMessageID();
				EmailMessageDTO message = messageCache.get(messageId);
				if (message == null) {
					message = rrService.getMessage(messageId);
					messageCache.put(messageId, message);
				}
				String result = "";
				boolean success = false;
				try {
					success = emailSender.sendMail(message, er.getEmail());
				} catch (Exception e ) {
					result = e.toString();
				}
				if (success) {
					System.out.println("success");
					result = "1";
					rrService.recipientHandledSuccess(er, result);
					sent ++;
				} else {
					System.out.println("failure");
					result = "0";
					rrService.recipientHandledFailure(er, result);
					errors ++;
				}
			}
			long took = System.currentTimeMillis() - vStart;
			System.out.println("Message handling Took " + took);
		}
		long took = System.currentTimeMillis() - start;
		log.info("Sending emails done. Queue: " + queueSize + ", sent: "+ sent +", errors: "+errors + ", took: "+took+" ms.");
	}
}
