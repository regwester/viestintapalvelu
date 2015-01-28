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
package fi.vm.sade.viestintapalvelu.address;

import fi.vm.sade.viestintapalvelu.IPostCountryCodes;
import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

import org.apache.commons.lang.StringEscapeUtils;

public class XmlAddressLabelDecorator extends AddressLabelDecorator {

    public XmlAddressLabelDecorator(AddressLabel addressLabel) {
        super(addressLabel);
    }

    public String getFirstName() {
        return escape(decoratedLabel.getFirstName());
    }

    public String getLastName() {
        return escape(decoratedLabel.getLastName());
    }

    public String getAddressline() {
        return decorateAddressField(decoratedLabel.getAddressline());
    }

    public String getAddressline2() {
        return decorateAddressField(decoratedLabel.getAddressline2());
    }

    public String getAddressline3() {
        return decorateAddressField(decoratedLabel.getAddressline3());
    }

    public String getPostalCode() {
        return escape(decoratedLabel.getPostalCode());
    }

    public String getCity() {
        return escape(decoratedLabel.getCity());
    }

    public String getRegion() {
        return escape(decoratedLabel.getRegion());
    }

    public String getCountry() {
        return decorateCountry(decoratedLabel.getCountry());
    }

    public String getCountryCode() {
        return escape(IPostCountryCodes.iso3CountryCodeToIso2CountryCode(decoratedLabel.getCountryCode()));
    }

    @Override
    protected String escape(String text) {
        return StringEscapeUtils.escapeXml(text);
    }
}
