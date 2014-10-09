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

package fi.vm.sade.viestintapalvelu.letter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.viestintapalvelu.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.component.CurrentUserComponent;
import fi.vm.sade.viestintapalvelu.letter.impl.DokumenttiIdProviderImpl;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

/**
 * User: ratamaa
 * Date: 9.10.2014
 * Time: 14:22
 */
@RunWith(JUnit4.class)
public class DokumenttiIdProviderImplTest {
    private DokumenttiIdProviderImpl dokumenttiIdProvider = new DokumenttiIdProviderImpl();

    @Before
    public void befor() {
        final Henkilo henkilo = DocumentProviderTestData.getHenkilo();
        dokumenttiIdProvider.setCurrentUserComponent(new CurrentUserComponent() {
            public Henkilo getCurrentUser() {
                return henkilo;
            }
        });
    }

    // TODO
}
