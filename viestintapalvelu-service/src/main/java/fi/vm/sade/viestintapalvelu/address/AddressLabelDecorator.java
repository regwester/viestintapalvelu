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

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

public abstract class AddressLabelDecorator {

    protected AddressLabel decoratedLabel;

    public AddressLabelDecorator(AddressLabel addressLabel) {
        this.decoratedLabel = addressLabel;
    }

    protected abstract String escape(String text);

    protected String decorateAddressField(String textString) {
        return textString != null && !"".equals(textString.trim()) ? escape(
                textString).replace("\n", "<br/>") : "";
    }

    protected String decorateCountry(String country) {
        return isDomestic(country) ? "" : escape(country);
    }

    private boolean isDomestic(String country) {
        return country == null || country.equalsIgnoreCase("FINLAND")
                || country.equalsIgnoreCase("SUOMI");
    }
}
