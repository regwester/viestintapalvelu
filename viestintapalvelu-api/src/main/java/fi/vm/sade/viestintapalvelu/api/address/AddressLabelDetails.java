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
package fi.vm.sade.viestintapalvelu.api.address;

/**
 * User: ratamaa
 * Date: 18.9.2014
 * Time: 14:51
 */
public interface AddressLabelDetails {
    String getFirstName();

    String getLastName();

    String getAddressline();

    String getAddressline2();

    String getAddressline3();

    String getPostalCode();

    String getCity();

    String getRegion();

    String getCountry();

    String getCountryCode();
}
