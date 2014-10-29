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

package fi.vm.sade.viestintapalvelu.asiontitili.api.dto;

import java.io.Serializable;

/**
 * User: ratamaa
 * Date: 27.10.2014
 * Time: 10:03
 */
public class AsiointitiliReceiverStatusDto implements Serializable {
    private static final long serialVersionUID = 6247663062212940608L;

    private String id;
    private String receiverOid;
    private boolean asiointitiliExists=false;
    private int stateCode;
    private String stateDescription;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReceiverOid() {
        return receiverOid;
    }

    public void setReceiverOid(String receiverOid) {
        this.receiverOid = receiverOid;
    }

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateDescription() {
        return stateDescription;
    }

    public void setStateDescription(String stateDescription) {
        this.stateDescription = stateDescription;
    }

    public boolean isAsiointitiliExists() {
        return asiointitiliExists;
    }

    public void setAsiointitiliExists(boolean asiointitiliExists) {
        this.asiointitiliExists = asiointitiliExists;
    }
}
