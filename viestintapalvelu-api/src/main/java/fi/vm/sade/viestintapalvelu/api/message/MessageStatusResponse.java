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
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author risal1
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Viestin tila")
public class MessageStatusResponse implements Serializable {

    private static final long serialVersionUID = 9067950805818927163L;

    public enum State {
        PROCESSED, PROCESSING, ERROR;
    }
    
    @ApiModelProperty(value = "tila")
    public final State state;
    
    @ApiModelProperty(value = "url mistä viestin tilaa voi kysellä")
    public final String statusUrl;
    
    @ApiModelProperty(value = "Virheviesti")
    public final String errorMessage;
    
    @ApiModelProperty(value = "Vastaanottajien tila (esim. onnistuiko lähetys kyseiselle vastaanottajalle")
    public final List<ReceiverStatus> receiverStatuses;

    public MessageStatusResponse(State state, String errorMessage, String statusUrl, List<ReceiverStatus> receiverStatuses) {
        this.errorMessage = errorMessage;
        this.state = state;
        this.statusUrl = statusUrl;
        this.receiverStatuses = receiverStatuses;
    }

    @Override
    public String toString() {
        return "MessageStatusResponse [state=" + state + ", statusUrl=" + statusUrl + ", errorMessage=" + errorMessage + ", receiverStatuses="
                + receiverStatuses + "]";
    }

}
