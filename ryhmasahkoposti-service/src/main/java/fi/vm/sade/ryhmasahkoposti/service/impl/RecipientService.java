package fi.vm.sade.ryhmasahkoposti.service.impl;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedRecipientDAO;
import fi.vm.sade.ryhmasahkoposti.exception.PersistenceException;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.OrganizationComponent;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.PersonComponent;
import fi.vm.sade.ryhmasahkoposti.model.ReportedRecipient;
import fi.vm.sade.ryhmasahkoposti.validation.OidValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RecipientService {
    private static Logger log = LoggerFactory.getLogger(RecipientService.class);

    @Autowired
    private PersonComponent personComponent;
    @Autowired
    private OrganizationComponent organizationComponent;
    @Autowired
    private ReportedRecipientDAO recipientDAO;
    @Autowired
    private ApplicationContext applicationContext;

    //TODO: request the recipient entities as batch from Henkilöpalvelu and Organisaatiopalvelu
    //e.g. give a list of oids and receive a list of Henkilö or organization objects
    @Scheduled(cron = "0 0/1 * * * ?") // Run every 5 minutes
    public void retrieveMissingInformation() {
        log.debug("Started retrieving missing recipient information");
        RecipientService self = applicationContext.getBean(RecipientService.class);
        try {
            List<Long> recipientIds = recipientDAO.findRecipientIdsWithIncompleteInformation();
            for(Long recipientId : recipientIds) {
                self.updateRecipientInformation(recipientId);
            }
        } catch (Exception e) {
            log.error("Information retrieval failed", e);
        }
    }

    @Transactional
    public void updateRecipientInformation(Long recipientId) {
        ReportedRecipient recipient = recipientDAO.findByRecipientID(recipientId);
        if (OidValidator.isHenkiloOID(recipient.getRecipientOid())) {
            updatePerson(recipient);
        } else if(OidValidator.isOrganisaatioOID(recipient.getRecipientOid())) {
            updateOrganization(recipient);
        } else {
            log.warn("Unrecognizable OID: {}, in recipient: {}",
                    recipient.getRecipientOid(), recipient.getRecipientEmail());
        }
    }

    private void updateOrganization(ReportedRecipient recipient) {
        log.debug("Updating organization recipient with oid {} ", recipient.getRecipientOid());
        OrganisaatioRDTO organisaatio = organizationComponent.getOrganization(recipient.getRecipientOid());
        String nameOfOrganisation = organizationComponent.getNameOfOrganisation(organisaatio);
        recipient.setSearchName(nameOfOrganisation);
        recipient.setDetailsRetrieved(true);
        updateRecipient(recipient);
    }

    private void updatePerson(ReportedRecipient recipient) {
        log.debug("Updating person recipient with oid {} ", recipient.getRecipientOid());
        Henkilo henkilo = personComponent.getPerson(recipient.getRecipientOid());
        recipient.setSearchName(henkilo.getSukunimi() + "," + henkilo.getEtunimet());
        recipient.setSocialSecurityID(henkilo.getHetu());
        recipient.setDetailsRetrieved(true);
        updateRecipient(recipient);
    }

    private void updateRecipient(ReportedRecipient recipient) {
        try {
            log.debug("Updating recipient to db: {}", recipient);
            recipientDAO.update(recipient);
        } catch(Exception e) {
            log.error("Reciepient update failed", e);
        }
    }

}
