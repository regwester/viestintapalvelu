package fi.vm.sade.ryhmasahkoposti.service.impl;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounce;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounces;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.BounceResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.BounceComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.EmailBounceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class EmailBounceServiceImpl implements EmailBounceService {
    private static final Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailBounceServiceImpl.class);

    @Autowired
    private BounceComponent bounceComponent;

    @Autowired
    private ReportedRecipientDAO reportedRecipientDAO;

    @Override
    public void checkEmailBounces() {
        final EmailBounces bounces = bounceComponent.getBounces();
        log.info("Checking email bounces...");
        for (EmailBounce b: bounces.getBouncedEmails()) {
            log.info("Found email bounce for address={}, messageId={}", b.getEmail(), b.getMessageId());
            final List<ReportedRecipient> byLetterHash = reportedRecipientDAO.findByLetterHash(convertToLetterHash(b.getMessageId()));
            if (byLetterHash.size() == 0 ) {
                log.warn("Email bounce not found from db email={}, messageId={}", b.getEmail(), b.getMessageId());
            }
            for (ReportedRecipient recipient: byLetterHash) {
                log.info("Bounce for address {} found from db", recipient.getRecipientEmail());
            }
        }
    }

    private String convertToLetterHash(String messageId) {
        return messageId.replace(".posti@hard.ware.fi", "");
    }
}
