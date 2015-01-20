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
import java.util.Map;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author risal1
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Viestin tiedot")
public class MessageData implements Serializable {

    private static final long serialVersionUID = 4631417294997381168L;
    
    @NotNull
    @ApiModelProperty(value = "Kirjepohjan nimi", required = true)
    public final String templateName;
    
    @NotNull
    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'", required = true)
    public final String language;
    
    @NotNull
    @ApiModelProperty(value = "Vastaanottajat", required = true)
    public final List<Receiver> receivers;
    
    @NotNull
    @ApiModelProperty(value = "Kaikille vastaanottajille yhteiset korvauskentät", required = true)
    public final Map<String, Object> commonReplacements;
    
    @ApiModelProperty(value = "Lähettäjän tunniste")
    public final String senderOid;
    
    @ApiModelProperty(value = "Organisaation tunniste")
    public final String organizationOid;
    
    @ApiModelProperty(value = "Hakukohteen tunniste")
    public final String applicationTargetOid;

    public MessageData(String templateName, String language, List<Receiver> receivers, Map<String, Object> commonReplacements, String senderOid, String organizationOid, String applicationTargetOid) {
        this.templateName = templateName;
        this.language = language;
        this.receivers = receivers;
        this.commonReplacements = commonReplacements;
        this.senderOid = senderOid;
        this.organizationOid = organizationOid;
        this.applicationTargetOid = applicationTargetOid;
    }
    
    public MessageData(String templateName, List<Receiver> receivers, Map<String, Object> commonReplacements) {
        this(templateName, "FI", receivers, commonReplacements, null, null, null);
    }
   
    @SuppressWarnings("unused")
    private MessageData() {
        this(null, null, null);
    }

    public MessageData copyOf(List<Receiver> receivers) {
        return new MessageData(this.templateName, this.language, receivers, this.commonReplacements, this.senderOid, this.organizationOid, this.applicationTargetOid);
    }

    @Override
    public String toString() {
        return "MessageData [templateName=" + templateName + ", language=" + language + ", receivers=" + receivers + ", commonReplacements="
                + commonReplacements + ", senderOid=" + senderOid + ", organizationOid=" + organizationOid + ", applicationTargetOid=" + applicationTargetOid
                + "]";
    }

}
