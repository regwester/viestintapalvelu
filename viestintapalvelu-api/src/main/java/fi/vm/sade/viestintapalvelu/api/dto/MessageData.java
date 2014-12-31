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
package fi.vm.sade.viestintapalvelu.api.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

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
    
    @ApiModelProperty(value = "Kirjepohjan nimi")
    public final String templateName;
    
    @ApiModelProperty(value = "Kielikoodi ISO 639-1, default = 'FI'")
    public final String language;
    
    @ApiModelProperty(value = "Vastaanottajat")
    public final List<Receiver> receivers;

    @ApiModelProperty(value = "Kaikille vastaanottajille yhteiset korvauskentät")
    public final Map<String, Object> commonReplacements;

    public MessageData(String templateName, String language, List<Receiver> receivers, Map<String, Object> commonReplacements) {
        this.templateName = templateName;
        this.language = language;
        this.receivers = receivers;
        this.commonReplacements = commonReplacements;
    }
    
    public MessageData(String templateName, List<Receiver> receivers, Map<String, Object> commonReplacements) {
        this(templateName, "FI", receivers, commonReplacements);
    }

    @Override
    public String toString() {
        return "MessageData [templateName=" + templateName + ", language=" + language + ", receivers=" + receivers + ", commonReplacements="
                + commonReplacements + "]";
    }

}
