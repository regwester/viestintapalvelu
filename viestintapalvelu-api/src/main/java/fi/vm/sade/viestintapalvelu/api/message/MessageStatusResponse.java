/**
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
package fi.vm.sade.viestintapalvelu.api.message;

import java.io.Serializable;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author risal1
 *
 */
@ApiModel(value = "Viestin tila")
public class MessageStatusResponse implements Serializable {
    
    public enum State {
        PROCESSED, PROCESSING, ERROR;
    }
    
    @ApiModelProperty(value = "Virheviesti")
    public final String errorMessage;
    
    @ApiModelProperty(value = "tila")
    public final State state;
    
    @ApiModelProperty(value = "url mistä viestin tilaa voi kysellä")
    public final String statusUrl;

    public MessageStatusResponse(String errorMessage, State state, String statusUrl) {
        this.errorMessage = errorMessage;
        this.state = state;
        this.statusUrl = statusUrl;
    }

    @Override
    public String toString() {
        return "MessageStatusResponse [errorMessage=" + errorMessage + ", state=" + state + ", statusUrl=" + statusUrl + "]";
    }
    
}
