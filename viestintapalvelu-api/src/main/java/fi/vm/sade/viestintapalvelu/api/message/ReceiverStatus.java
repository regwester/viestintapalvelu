/**
 * Copyright (c) 2015 The Finnish National Board of Education - Opetushallitus
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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author risal1
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Viestin vastaanottajan tila")
public class ReceiverStatus {

    public enum SendMethod {
        ASIOINTITILI, EMAIL
    }
    
    @ApiModelProperty(value = "vastaanottajan tunniste")
    public final String oid;
    
    @ApiModelProperty(value = "viestinvälitys tapa mitä käytettiin")
    public final SendMethod sendMethod;
    
    @ApiModelProperty(value = "epäonnistuiko lähetys")
    public final boolean sendingFailed;
    
    @ApiModelProperty(value = "vastaanottajakohtainen virheviesti", required = false)
    public final String errorMessage;

    public ReceiverStatus(String oid, SendMethod sendMethod, boolean sendingFailed, String errorMessage) {
        this.oid = oid;
        this.sendMethod = sendMethod;
        this.sendingFailed = sendingFailed;
        this.errorMessage = errorMessage;
    }
    
    public ReceiverStatus(String oid, SendMethod sendMethod) {
        this(oid, sendMethod, false, null);
    }

    @Override
    public String toString() {
        return "ReceiverStatus [oid=" + oid + ", sendMethod=" + sendMethod + ", sendingFailed=" + sendingFailed + ", errorMessage=" + errorMessage + "]";
    }
    
}
