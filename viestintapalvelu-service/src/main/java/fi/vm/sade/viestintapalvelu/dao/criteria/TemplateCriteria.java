/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
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

package fi.vm.sade.viestintapalvelu.dao.criteria;

import fi.vm.sade.viestintapalvelu.model.Template.State;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;

/**
 * User: ratamaa
 * Date: 9.9.2014
 * Time: 10:29
 */
public interface TemplateCriteria {

    /**
     * @return name used with this criteria to filter the result (null if not used)
     */
    String getName();

    /**
     * @return the uppercase 2-letter language code used with this criteria to filter the result (null if not used)
     */
    String getLanguage();

    /**
     * @return the document type used with this criteria to filter the result (null if not used)
     */
    ContentStructureType getType();

    /**
     * @return the OID of a Haku used with this criteria to filter the result (null if not used)
     */
    String getApplicationPeriod();
    
    /**
     * @return the state used with this criteria to filter the result (defaults to {@link State#julkaistu}
     */
    State getState();

    /**
     * @return true if the matching template should be the default one (for given name and language)
     */
    boolean isDefaultRequired();

    /**
     * @param name to use (or not to use if null)
     * @return a criteria with name condition set to given value
     */
    TemplateCriteria withName(String name);

    /**
     * @param language to use (or not to use if null)
     * @return a criteria with language condition set to given value
     */
    TemplateCriteria withLanguage(String language);

    /**
     * @param type to use (or not to use if null)
     * @return a criteria with type condition set to given value
     */
    TemplateCriteria withType(ContentStructureType type);

    /**
     * @param hakuOid to use (or not to use if null)
     * @return a criteria with Haku OID condition set to given value
     */
    TemplateCriteria withApplicationPeriod(String hakuOid);

    /**
     * @return a criteria with default requirement
     */
    TemplateCriteria withDefaultRequired();

    /**
     * @return a criteria without default requirement
     */
    TemplateCriteria withoutDefaultRequired();
    
    /**
     * @param {@link State} to use (or not to use if null
     * @return a criteria with state condition set to given value
     */
    TemplateCriteria withState(State state);
}
