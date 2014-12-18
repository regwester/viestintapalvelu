/**
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
package fi.vm.sade.viestintapalvelu.person;

import java.io.Serializable;

import fi.vm.sade.authentication.model.Henkilo;

/**
 * @author risal1
 *
 */
public class Person implements Serializable {
    
    private static final long serialVersionUID = -8920891120706972390L;

    public final String firstNames;
    
    public final String lastName;
    
    public final String oid;

    public Person(String firstNames, String lastName, String oid) {
        this.firstNames = firstNames;
        this.lastName = lastName;
        this.oid = oid;
    }
    
    public Person(Henkilo henkilo) {
        this(henkilo.getEtunimet(), henkilo.getSukunimi(), henkilo.getOidHenkilo());
    }

}
