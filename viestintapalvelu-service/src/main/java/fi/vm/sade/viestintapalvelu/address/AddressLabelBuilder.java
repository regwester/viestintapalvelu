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

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import fi.vm.sade.viestintapalvelu.Constants;

import org.springframework.stereotype.Service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.lowagie.text.DocumentException;

import fi.vm.sade.viestintapalvelu.api.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.document.DocumentBuilder;

@Service
@Singleton
public class AddressLabelBuilder {

    private DocumentBuilder documentBuilder;

    @Inject
    public AddressLabelBuilder(DocumentBuilder documentBuilder) {
        this.documentBuilder = documentBuilder;
    }

    public byte[] printPDF(AddressLabelBatch input) throws DocumentException, IOException {
        List<AddressLabel> addresses = input.getAddressLabels();
        Collections.sort(addresses);
        Map<String, Object> context = createDataContext(addresses, new AddressLabelDecoratorBuilder() {
            protected AddressLabelDecorator newAddressLabelDecorator(AddressLabel addressLabel) {
                return new HtmlAddressLabelDecorator(addressLabel);
            }
        });
        byte[] xhtml = documentBuilder.applyTextTemplate(Constants.ADDRESS_LABEL_PDF_TEMPLATE, context);
        return documentBuilder.xhtmlToPDF(xhtml);
    }

    public byte[] printCSV(AddressLabelBatch input) throws IOException {
        Map<String, Object> context = createDataContext(input.getAddressLabels(), new AddressLabelDecoratorBuilder() {
            protected AddressLabelDecorator newAddressLabelDecorator(AddressLabel addressLabel) {
                return new XmlAddressLabelDecorator(addressLabel);
            }
        });
        return documentBuilder.applyTextTemplate(Constants.ADDRESS_LABEL_XLS_TEMPLATE, context);
    }

    private Map<String, Object> createDataContext(List<AddressLabel> addressLabels,
            final AddressLabelDecoratorBuilder builder) {
        Map<String, Object> data = new HashMap<>();
        data.put("labelList", Lists.transform(addressLabels, new Function<AddressLabel, AddressLabelDecorator>() {
            public AddressLabelDecorator apply(AddressLabel label) {
                return builder.newAddressLabelDecorator(label);
            }
        }));
        return data;
    }

    private abstract class AddressLabelDecoratorBuilder {
        protected abstract AddressLabelDecorator newAddressLabelDecorator(AddressLabel addressLabel);
    }
}
