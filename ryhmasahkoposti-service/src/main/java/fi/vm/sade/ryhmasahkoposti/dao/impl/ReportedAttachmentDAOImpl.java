/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.ryhmasahkoposti.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.ryhmasahkoposti.dao.ReportedAttachmentDAO;
import fi.vm.sade.ryhmasahkoposti.model.ReportedAttachment;
import fi.vm.sade.ryhmasahkoposti.model.ReportedMessageRecipientAttachment;

@Repository
public class ReportedAttachmentDAOImpl extends AbstractJpaDAOImpl<ReportedAttachment, Long> implements ReportedAttachmentDAO {

    @Override
    public List<Long> saveReportedAttachments(List<ReportedAttachment> attachments) {
        List<Long> ids = new ArrayList<>();
        for (ReportedAttachment attachment : attachments) {
            getEntityManager().persist(attachment);
            ids.add(attachment.getId());
        }
        getEntityManager().flush();
        return ids;
    }

    @Override
    public void insert(ReportedMessageRecipientAttachment recipientAttachment) {
        getEntityManager().persist(recipientAttachment);
        getEntityManager().flush();
    }
}
