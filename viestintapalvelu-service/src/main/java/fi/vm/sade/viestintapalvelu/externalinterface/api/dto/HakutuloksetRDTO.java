/*
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
 */

package fi.vm.sade.viestintapalvelu.externalinterface.api.dto;

import java.util.List;
import java.util.Map;

/**
 * Created by jonimake on 16.1.2015.
 */
public class HakutuloksetRDTO<T> {
    private int tuloksia;
    private List<T> tulokset;

    public int getTuloksia() {
        return tuloksia;
    }

    public void setTuloksia(int tuloksia) {
        this.tuloksia = tuloksia;
    }

    public List<T> getTulokset() {
        return tulokset;
    }

    public void setTulokset(List<T> tulokset) {
        this.tulokset = tulokset;
    }

}

