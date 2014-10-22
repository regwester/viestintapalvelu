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

package fi.vm.sade.viestintapalvelu.util.dtoconverter;

import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;

import fi.ratamaa.dtoconverter.typeconverter.TypeConversionContainer;
import fi.ratamaa.dtoconverter.typeconverter.TypeConversionSuite;
import fi.ratamaa.dtoconverter.typeconverter.TypeConverterAdapter;
import fi.vm.sade.viestintapalvelu.util.XMLTypeHelper;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 10:10
 */
public class XmlDateTypeConversions implements TypeConversionSuite {
    @Override
    public void registerConverters(TypeConversionContainer conversions) {
        conversions.add(DateTime.class, XMLGregorianCalendar.class, new TypeConverterAdapter<DateTime, XMLGregorianCalendar>() {
            @Override
            public XMLGregorianCalendar convert(DateTime dt) {
                return XMLTypeHelper.dateTime(dt);
            }
        }).add(XMLGregorianCalendar.class, DateTime.class, new TypeConverterAdapter<XMLGregorianCalendar, DateTime>() {
            @Override
            public DateTime convert(XMLGregorianCalendar dt) {
                if (dt == null) {
                    return null;
                }
                return new DateTime(dt.toGregorianCalendar());
            }
        });
    }
}
