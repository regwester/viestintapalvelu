/**
 * Copyright (c) 2015 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */
package fi.vm.sade.viestintapalvelu.message.conversion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fi.vm.sade.ryhmasahkoposti.api.dto.EmailData;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailMessage;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailRecipient;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReplacementDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.ReportedRecipientReplacementDTO;
import fi.vm.sade.viestintapalvelu.api.message.MessageData;
import fi.vm.sade.viestintapalvelu.api.message.Receiver;

/**
 * @author risal1
 *
 */
public class MessageToEmailConverter implements MessageDataConverter<MessageData, EmailData> {

    @Override
    public ConvertedMessageWrapper<EmailData> convert(MessageData data) {
        EmailData email = new EmailData();
        email.setEmail(convertToEmailMessage(data));
        email.setReplacements(convertToEmailReplacements(data.commonReplacements));
        List<Receiver> incompatibleReceivers = filterIncompatibleReceivers(data);
        email.setRecipient(convertToEmailRecipients(filterCompatibleReceivers(data, incompatibleReceivers)));
        return new ConvertedMessageWrapper<>(email, incompatibleReceivers);
    }

    private List<Receiver> filterCompatibleReceivers(MessageData data, List<Receiver> incompatibleReceivers) {
        List<Receiver> receiversToProcess = new ArrayList<>(data.receivers);
        receiversToProcess.removeAll(incompatibleReceivers);
        return receiversToProcess;
    }

    private List<Receiver> filterIncompatibleReceivers(MessageData data) {
        return ImmutableList.copyOf(Iterables.filter(data.receivers, new Predicate<Receiver>() {

            @Override
            public boolean apply(Receiver input) {
                return StringUtils.isBlank(input.email);
            }

        }));
    }

    private List<EmailRecipient> convertToEmailRecipients(List<Receiver> receivers) {
        return ImmutableList.copyOf(Lists.transform(receivers, new Function<Receiver, EmailRecipient>() {

            @Override
            public EmailRecipient apply(Receiver input) {
                EmailRecipient recipient = new EmailRecipient(input.oid, input.email);
                recipient.setLanguageCode(input.language);
                recipient.setRecipientReplacements(convertRecipientReplacements(input.replacements));
                return recipient;
            }

            private List<ReportedRecipientReplacementDTO> convertRecipientReplacements(Map<String, Object> replacements) {
                List<ReportedRecipientReplacementDTO> repls = new ArrayList<>();
                for (String key : replacements.keySet()) {
                    repls.add(new ReportedRecipientReplacementDTO(key, replacements.get(key)));
                }
                return repls;
            }
            
        }));
    }

    private List<ReplacementDTO> convertToEmailReplacements(Map<String, Object> commonReplacements) {
        List<ReplacementDTO> repls = new ArrayList<>();
        for (String key : commonReplacements.keySet()) {
            if (commonReplacements.get(key) instanceof String) {
                ReplacementDTO repl = new ReplacementDTO();
                repl.setName(key);
                repl.setDefaultValue((String) commonReplacements.get(key));
                repls.add(repl);
            }
        }
        return repls;
    }

    private EmailMessage convertToEmailMessage(MessageData data) {
        EmailMessage message = new EmailMessage();
        message.setTemplateName(data.templateName);
        message.setLanguageCode(data.language);
        message.setSenderOid(data.senderOid);
        message.setOrganizationOid(data.organizationOid);
        message.setHakuOid(data.applicationTargetOid);
        return message;        
    }

}
