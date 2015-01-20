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
package fi.vm.sade.viestintapalvelu.template;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * @author risal1
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Kirjeluonnoksen päivitykseen tarkoitettu dto")
public class DraftUpdateDTO implements Serializable {

    
    private static final long serialVersionUID = 7984078945532954831L;

    @ApiModelProperty(value = "kirjeluonnoksen tunniste", required = true)
    public final long id;
    
    @ApiModelProperty(value = "kirjeluonnoksen sisältö-nimisen korvauskentän sisältö", required = true)
    public final String content;

    @ApiModelProperty(value = "organisaation tunniste johon kirjeluonnos liittyy", required = true)
    public final String orgoid;

    public DraftUpdateDTO(long id, String content, String orgoid) {
        this.id = id;
        this.content = content;
        this.orgoid = orgoid;
    }
    
    @SuppressWarnings("unused")
    private DraftUpdateDTO() {
        this(0,null,null);
    }

    @Override
    public String toString() {
        return "DraftUpdateDTO [id=" + id + ", content=" + content + ", orgoid=" + orgoid + "]";
    }
}
