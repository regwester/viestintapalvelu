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
package fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.converter;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import fi.ratamaa.dtoconverter.typeconverter.TypeConversionContainer;
import fi.ratamaa.dtoconverter.typeconverter.TypeConverterAdapter;
import fi.suomi.asiointitili.*;
import fi.vm.sade.viestintapalvelu.externalinterface.asiointitili.dto.*;
import fi.vm.sade.viestintapalvelu.util.dtoconverter.AbstractDtoConverter;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 9:41
 */
@Component
public class AsiointitiliDtoConverter extends AbstractDtoConverter {

    @Override
    protected void registerConverters(TypeConversionContainer conversions) {
        super.registerConverters(conversions);

        conversions.add(Boolean.class, String.class, new TypeConverterAdapter<Boolean, String>() {
            @Override
            public String convert(Boolean obj) {
                return obj == null ? null : (obj ? "1" : "0");
            }
        });
    }

    public ArrayOfKohdeWS2 convert(ArrayList<KohdeDto> from, ArrayOfKohdeWS2 to) {
        to.getKohde().clear();
        to.getKohde().addAll(convertCollection(from, new ArrayList<KohdeWS2>(), KohdeWS2.class));
        return to;
    }

    public ArrayOfTiedosto convert(ArrayList<TiedostoDto> from, ArrayOfTiedosto to) {
        to.getTiedosto().clear();
        to.getTiedosto().addAll(convertCollection(from, new ArrayList<Tiedosto>(), Tiedosto.class));
        return to;
    }

    public ArrayOfAsiakas convert(ArrayList<AsiakasDto> from, ArrayOfAsiakas to) {
        to.getAsiakas().clear();
        to.getAsiakas().addAll(convertCollection(from, new ArrayList<Asiakas>(), Asiakas.class));
        return to;
    }

    public ArrayList<KohdeJaAsiakasTilaDto> convert(ArrayOfKohdeJaAsiakasTilaWS2V from, ArrayList<KohdeJaAsiakasTilaDto> to) {
        return convertCollection(from.getKohde(), to, KohdeJaAsiakasTilaDto.class);
    }

    public ArrayList<AsiakasTilaDto> convert(ArrayOfAsiakasJaTilaWS1 from, ArrayList<AsiakasTilaDto> to) {
        return convertCollection(from.getAsiakas(), to, AsiakasTilaDto.class);
    }

    public KyselyWS2 convert(KohdeLisaysDto from, KyselyWS2 to) {
        convertValue(from, to);
        to.setKohdeMaara(to.getKohteet().getKohde().size());
        return to;
    }

}
