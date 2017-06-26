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

import org.apache.commons.lang.StringEscapeUtils;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;

public class HtmlAddressLabelDecorator extends AddressLabelDecorator {

    public HtmlAddressLabelDecorator(AddressLabel addressLabel) {
        super(addressLabel);
    }

    private String decorateAddressline(String textString) {
        return textString != null && !"".equals(textString.trim()) ? decorateAddressField(textString) + "<br/>" : "";
    }

    public String getAddress() {
        return decorateAddressline(decoratedLabel.getFirstName() + " <b>" + decoratedLabel.getLastName() + "</b>")
                + decorateAddressline(decoratedLabel.getAddressline()) + decorateAddressline(decoratedLabel.getAddressline2())
                + decorateAddressline(decoratedLabel.getAddressline3())
                + decorateAddressline(decoratedLabel.getPostalCode() + " " + decoratedLabel.getCity()) + decorateAddressline(decoratedLabel.getRegion())
                + decorateCountry(decoratedLabel.getCountry());
    }

    public String toString() {
        return getAddress();
    }

    @Override
    protected String escape(String text) {
        return StringEscapeUtils.escapeHtml(text);
    }
}
