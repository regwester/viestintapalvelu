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
package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.exception.ExternalInterfaceException;
import fi.vm.sade.viestintapalvelu.externalinterface.api.TarjontaHakuResource;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuDetailsDto;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakuRDTO;
import fi.vm.sade.viestintapalvelu.externalinterface.api.dto.HakukohdeDTO;

/**
 * User: ratamaa
 * Date: 7.10.2014
 * Time: 12:44
 */
@Component
public class TarjontaComponent {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(TarjontaComponent.class);
    private static final String DELETED_STATE = "POISTETTU";
    private static final Integer MAX_COUNT = 10000;
    private static final Comparator<? super HakuDetailsDto> HAKUS_IN_FINNISH_ORDER = new Comparator<HakuDetailsDto>() {
        public int compare(HakuDetailsDto o1, HakuDetailsDto o2) {
            return Optional.fromNullable(o1.getNimi().get("kieli_fi")).or("").compareTo(Optional.fromNullable(o2.getNimi().get("kieli_fi")).or(""));
        }
    };

    @Resource
    private TarjontaHakuResource tarjontaHakuResourceClient;

    public List<HakuDetailsDto> findPublished(Integer countLimit) {
        try {
            final HakuRDTO<List<HakuDetailsDto>> hakus1 = tarjontaHakuResourceClient.hakus(Optional.fromNullable(countLimit).or(MAX_COUNT));
            List<HakuDetailsDto> hakus = hakus1.getResult();
            List<HakuDetailsDto> hakuDetails = new ArrayList<HakuDetailsDto>();
            for (HakuDetailsDto haku : hakus) {
                if (!DELETED_STATE.equals(haku.getTila())) {
                    hakuDetails.add(haku);
                }
            }
            Collections.sort(hakuDetails, HAKUS_IN_FINNISH_ORDER);
            return hakuDetails;
        } catch (Exception e) {
            log.error("Error getting published hakus", e);
            throw new ExternalInterfaceException("Error fetching list of tarjonta's hakus", e);
        }
    }

    public Set<String> getProviderOrgOids(String applicationPeriod) {
        try {
            final HakuRDTO<List<String>> hakuOrganizationOids = tarjontaHakuResourceClient.getHakuOrganizationOids(applicationPeriod);
            if(hakuOrganizationOids == null || hakuOrganizationOids.getResult() == null) return new HashSet<>();
            List<String> oids = hakuOrganizationOids.getResult();
            return new HashSet<>(oids);
        } catch (Exception e) {
            log.error("Error finding haku by application period", e);
            throw new ExternalInterfaceException(e);
        }
    }
    
    public HakuRDTO<HakukohdeDTO> getHakuKohdeByOid(String oid) {
        try {
            return tarjontaHakuResourceClient.getHakuhdeByOid(oid);
        } catch (Exception e) {
            log.error("Error finding fetchTargets by oid " + oid);
            throw new ExternalInterfaceException(e);
        }
    }
}
