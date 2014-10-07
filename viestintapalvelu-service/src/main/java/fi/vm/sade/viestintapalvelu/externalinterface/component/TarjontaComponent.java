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

package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.api.TarjontaHakuResource;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuDetailsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuListDto;

/**
 * User: ratamaa
 * Date: 7.10.2014
 * Time: 12:44
 */
@Component
public class TarjontaComponent {
    public static final String PUBLISHED_STATE = "JULKAISTU";
    private static final Comparator<? super HakuDetailsDto> HAKUS_IN_FINNISH_ORDER = new Comparator<HakuDetailsDto>() {
        public int compare(HakuDetailsDto o1, HakuDetailsDto o2) {
            return Optional.fromNullable(o1.getNimi().get("kieli_fi")).or("")
                    .compareTo(Optional.fromNullable(o2.getNimi().get("kieli_fi")).or(""));
        }
    };

    @Resource
    private TarjontaHakuResource tarjontaHakuResourceClient;

    public List<HakuDetailsDto> findPublisehedHakus() {
        try {
            List<HakuListDto> hakus = tarjontaHakuResourceClient.hakus();
            List<HakuDetailsDto> hakuDetails = new ArrayList<HakuDetailsDto>();
            for (HakuListDto haku : hakus) {
                HakuDetailsDto details = tarjontaHakuResourceClient.hakuByOid(haku.getOid());
                if (PUBLISHED_STATE.equals(details.getTila())) {
                    hakuDetails.add(details);
                }
            }
            Collections.sort(hakuDetails, HAKUS_IN_FINNISH_ORDER);
            return hakuDetails;
        } catch(Exception e) {
            throw new ExternalInterfaceException("Error fetching list of tarjonta's hakus", e);
        }
    }

}
