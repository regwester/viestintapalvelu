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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HakukohdeTuloksetRDTO{
    private String oid;
    private Integer version;
    private Map<String, String> nimi;
    private List<HakutulosRDTO> tulokset;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Map<String, String> getNimi() {
        return nimi;
    }

    public void setNimi(Map<String, String> nimi) {
        this.nimi = nimi;
    }

    public List<HakutulosRDTO> getTulokset() {
        return tulokset;
    }

    public void setTulokset(List<HakutulosRDTO> tulokset) {
        this.tulokset = tulokset;
    }

    public static class HakutulosRDTO{
        private String oid; //hakukohde oid;
        private Map<String, String> nimi;
        private Map<String, String> kausi;
        private Integer vuosi;
        private String tila;
        private String hakuOid;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public Map<String, String> getNimi() {
            return nimi;
        }

        public void setNimi(Map<String, String> nimi) {
            this.nimi = nimi;
        }

        public Map<String, String> getKausi() {
            return kausi;
        }

        public void setKausi(Map<String, String> kausi) {
            this.kausi = kausi;
        }

        public Integer getVuosi() {
            return vuosi;
        }

        public void setVuosi(Integer vuosi) {
            this.vuosi = vuosi;
        }

        public String getTila() {
            return tila;
        }

        public void setTila(String tila) {
            this.tila = tila;
        }

        public String getHakuOid() {
            return hakuOid;
        }

        public void setHakuOid(String hakuOid) {
            this.hakuOid = hakuOid;
        }
    }
}
