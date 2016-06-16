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
 **/
package fi.vm.sade.viestintapalvelu.model;

import java.io.Serializable;

import javax.persistence.Embeddable;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 10:27
 */
@Embeddable
public class ContentStructureContentId implements Serializable {
    private static final long serialVersionUID = 5079835252183041164L;

    private Long contentStructureId;
    private Long contentId;

    public Long getContentStructureId() {
        return contentStructureId;
    }

    public void setContentStructureId(Long contentStructureId) {
        this.contentStructureId = contentStructureId;
    }

    public Long getContentId() {
        return contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ContentStructureContentId)) return false;

        ContentStructureContentId that = (ContentStructureContentId) o;

        if (contentId != null ? !contentId.equals(that.contentId) : that.contentId != null) return false;
        return contentStructureId != null ? contentStructureId.equals(that.contentStructureId) : that.contentStructureId == null;

    }

    @Override
    public int hashCode() {
        int result = contentStructureId != null ? contentStructureId.hashCode() : 0;
        result = 31 * result + (contentId != null ? contentId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ContentStructureContentId{" +
                "contentStructureId=" + contentStructureId +
                ", contentId=" + contentId +
                '}';
    }
}
