package fi.vm.sade.viestintapalvelu.externalinterface.component;

import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;

public class ComponentUtil {

    public static String getOrganizationNameFromRDTO(OrganisaatioRDTO organisaatio) {
        String[] language = { "fi", "sv", "en" };
        for (String aLanguage : language) {
            String nameOfOrganisation = organisaatio.getNimi().get(aLanguage);
            if (nameOfOrganisation != null && !nameOfOrganisation.isEmpty()) {
                return nameOfOrganisation;
            }
        }

        return "";
    }

}
