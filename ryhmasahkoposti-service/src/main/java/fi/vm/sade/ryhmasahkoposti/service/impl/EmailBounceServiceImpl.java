package fi.vm.sade.ryhmasahkoposti.service.impl;

import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounce;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailBounces;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.BounceComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.service.EmailBounceService;
import fi.vm.sade.ryhmasahkoposti.service.ReportedRecipientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class EmailBounceServiceImpl implements EmailBounceService {
    private static final Logger log = LoggerFactory.getLogger(fi.vm.sade.ryhmasahkoposti.service.impl.EmailBounceServiceImpl.class);

    private boolean run;

    @Autowired
    private BounceComponent bounceComponent;

    @Autowired
    private ReportedRecipientService reportedRecipientService;

    @Override
    public void checkEmailBounces() {
        if (run) {
            runEmailBouncesCheck();
        }
    }

    private void runEmailBouncesCheck() {
        final EmailBounces bounces = bounceComponent.getBounces();
        log.info("Checking email bounces, count=" + bounces.getBouncedEmails().size());
        for (EmailBounce b: bounces.getBouncedEmails()) {
            log.info("Found bounce for email={}, messageId={}", b.getEmail(), b.getMessageId());
            final List<ReportedRecipient> byLetterHash = reportedRecipientService.findByLetterHash(convertToLetterHash(b.getMessageId()));
            if (byLetterHash.size() == 0 ) {
                log.warn("Bounce not found from db email={}, messageId={}", b.getEmail(), b.getMessageId());
            } else {
                handleBounce(byLetterHash);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void handleBounce(List<ReportedRecipient> bounced) {
        for (ReportedRecipient recipient: bounced) {
            log.info("Bounce email={}, messageId={} found from db", recipient.getRecipientEmail(), recipient.getLetterHash());
            if (!recipient.getSendingSuccessful().equals(GroupEmailConstants.SENDING_BOUNCED)) {
                log.info("Marking email={}, messageId={} as bounced", recipient.getRecipientEmail(), recipient.getLetterHash());
                recipient.setSendingSuccessful(GroupEmailConstants.SENDING_BOUNCED);
                reportedRecipientService.updateReportedRecipient(recipient);
            }
        }
    }

    private String convertToLetterHash(String messageId) {
        return messageId.replace(".posti@opintopolku.fi", "");
    }

    public boolean isRun() {
        return run;
    }

    public void setRun(boolean run) {
        this.run = run;
    }
}
