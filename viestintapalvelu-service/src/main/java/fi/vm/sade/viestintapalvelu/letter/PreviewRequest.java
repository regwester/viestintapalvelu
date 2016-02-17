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
 */
package fi.vm.sade.viestintapalvelu.letter;

import fi.vm.sade.viestintapalvelu.model.Template.State;

public class PreviewRequest {
    private int templateId;
    private State tamplateState;
    private String letterContent;
    
    public int getTemplateId() {
        return templateId;
    }
    public void setTemplateId(int templateId) {
        this.templateId = templateId;
    }
    public State getTemplateState() {
        return tamplateState;
    }
    public void setTemplateState(State state) {
        this.tamplateState = state;
    }
    public String getLetterContent() {
        return letterContent;
    }
    public void setLetterContent(String letterContents) {
        this.letterContent = letterContents;
    }
    @Override
    public String toString() {
        return "PreviewRequest [templateId=" + templateId + ", tamplateState=" + tamplateState + ", letterContents=" + letterContent + "]";
    }
    
}
