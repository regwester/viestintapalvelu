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
package fi.vm.sade.ryhmasahkoposti.validation;

import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.api.constants.GroupEmailConstants;

@Component
public class OidValidator {

    public static boolean isOID(String searchArgument) {
        String trimmedSearchArgument = searchArgument.trim();

        return trimmedSearchArgument.startsWith(GroupEmailConstants.OID_OPH_TREE);

    }

    public static boolean isHenkiloOID(String searchArgument) {
        String trimmedSearchArgument = searchArgument.trim();

        if (!trimmedSearchArgument.startsWith(GroupEmailConstants.OID_OPH_TREE)) {
            return false;
        }

        return trimmedSearchArgument.startsWith(GroupEmailConstants.OID_OPH_PERSON_TREE);

    }

    public static boolean isOrganisaatioOID(String searchArgument) {
        String trimmedsearchargument = searchArgument.trim();

        if (!isOID(trimmedsearchargument)) {
            return false;
        }

        return trimmedsearchargument.startsWith(GroupEmailConstants.OID_OPH_ORGANISATION_TREE);

    }
}
