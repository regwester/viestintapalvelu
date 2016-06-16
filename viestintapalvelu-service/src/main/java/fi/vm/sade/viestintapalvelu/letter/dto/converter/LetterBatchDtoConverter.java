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
package fi.vm.sade.viestintapalvelu.letter.dto.converter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabelDetails;
import fi.vm.sade.viestintapalvelu.externalinterface.common.ObjectMapperProvider;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterBatchDetails;
import fi.vm.sade.viestintapalvelu.letter.dto.LetterDetails;
import fi.vm.sade.viestintapalvelu.model.*;

/**
 * User: ratamaa Date: 18.9.2014 Time: 14:53
 */
@Component
public class LetterBatchDtoConverter {
    @Autowired
    private ObjectMapperProvider objectMapperProvider;

    public LetterBatch convert(LetterBatchDetails from, LetterBatch to, ObjectMapper mapper) throws JsonProcessingException {
        to.setTemplateId(from.getTemplateId());
        to.setTemplateName(from.getTemplateName());
        to.setApplicationPeriod(from.getApplicationPeriod());
        to.setFetchTarget(from.getFetchTarget());
        to.setLanguage(from.getLanguageCode());
        to.setOrganizationOid(from.getOrganizationOid());
        to.setTag(from.getTag());
        to.setIposti(from.isIposti());
        to.setSkipDokumenttipalvelu(from.isSkipDokumenttipalvelu());

        // kirjeet.lahetyskorvauskentat
        to.setLetterReplacements(parseLetterReplacementsModels(from, to, mapper));
        return to;
    }

    public Set<LetterReplacement> parseLetterReplacementsModels(LetterBatchDetails from, LetterBatch to, ObjectMapper mapper) throws JsonProcessingException {
        Set<LetterReplacement> replacements = new HashSet<>();

        Object replKeys[] = from.getTemplateReplacements().keySet().toArray();
        Object replVals[] = from.getTemplateReplacements().values().toArray();

        if (mapper == null) {
            mapper = objectMapperProvider.getContext(getClass());
        }

        for (int i = 0; i < replVals.length; i++) {
            LetterReplacement repl = new LetterReplacement();
            repl.setName(replKeys[i].toString());
            Object value = replVals[i];
            if (value instanceof String || value == null) {
                repl.setDefaultValue((String) value);
                repl.setJsonValue(null);
            } else {
                String json = mapper.writeValueAsString(value);
                repl.setJsonValue(json);
                repl.setDefaultValue(null);
            }
            repl.setTimestamp(new Date());
            repl.setLetterBatch(to);
            replacements.add(repl);
        }
        return replacements;
    }

    public LetterReceivers convert(LetterDetails from, LetterReceivers to, ObjectMapper mapper) throws JsonProcessingException {
        to.setTimestamp(new Date());
        to.setEmailAddress(from.getEmailAddress());
        to.setWantedLanguage(from.getLanguageCode());
        to.setSkipIPost(from.isSkipIPosti());
        to.setOidPerson(from.getPersonOid());
        to.setEmailAddressEPosti(from.getEmailAddressEPosti());

        if (mapper == null) {
            mapper = objectMapperProvider.getContext(getClass());
        }

        // kirjeet.vastaanottajakorvauskentat
        if ((from.getTemplateReplacements() != null) || (from.getTemplateReplacements().isEmpty())) {
            Set<LetterReceiverReplacement> letterRepl = new HashSet<>();

            Object letReplKeys[] = from.getTemplateReplacements().keySet().toArray();
            Object letReplVals[] = from.getTemplateReplacements().values().toArray();

            for (int i = 0; i < letReplVals.length; i++) {
                LetterReceiverReplacement repl = new LetterReceiverReplacement();

                repl.setName(letReplKeys[i].toString());
                Object value = letReplVals[i];
                if (value instanceof String || value == null) {
                    repl.setDefaultValue((String) value);
                    repl.setJsonValue(null);
                } else {
                    String json = mapper.writeValueAsString(value);
                    repl.setJsonValue(json);
                    repl.setDefaultValue(null);
                }
                // repl.setMandatory();
                repl.setTimestamp(new Date());
                repl.setLetterReceivers(to);
                letterRepl.add(repl);
            }
            to.setLetterReceiverReplacement(letterRepl);
        }

        // kirjeet.vastaanottajaosoite
        if (from.getAddressLabel() != null) {
            LetterReceiverAddress lra = convert(from.getAddressLabel(), new LetterReceiverAddress());
            lra.setLetterReceivers(to);
            to.setLetterReceiverAddress(lra);
        }

        return to;
    }

    public LetterReceiverAddress convert(AddressLabelDetails from, LetterReceiverAddress to) {
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setAddressline(from.getAddressline());
        to.setAddressline2(from.getAddressline2());
        to.setAddressline3(from.getAddressline3());
        to.setPostalCode(from.getPostalCode());
        to.setCity(from.getCity());
        to.setRegion(from.getRegion());
        to.setCountry(from.getCountry());
        to.setCountryCode(from.getCountryCode());
        return to;
    }

}
