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

package fi.vm.sade.viestintapalvelu.structure.dto.constraint;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.TypedContentStructureContent;

/**
* User: ratamaa
* Date: 13.11.2014
* Time: 14:27
*/
public class ContentStructurePredicate implements Predicate<TypedContentStructureContent> {
    private Set<ContentRole> roles = new HashSet<ContentRole>();
    private Integer max;
    private int consumed = 0;
    private Integer min;
    private Set<ContentType> types = new HashSet<ContentType>();
    private ContentStructurePredicate and;

    public ContentStructurePredicate(ContentRole... roles) {
        this.roles.addAll(Arrays.asList(roles));
    }

    public ContentStructurePredicate type(ContentType ...types) {
        this.types.addAll(Arrays.asList(types));
        return this;
    }

    public ContentStructurePredicate and(ContentStructurePredicate and) {
        this.and = and;
        return this;
    }

    public ContentStructurePredicate max(int max) {
        this.max = max;
        return this;
    }

    public ContentStructurePredicate min(int min) {
        this.min = min;
        return this;
    }

    public boolean consumes(TypedContentStructureContent dto) {
        if (!this.apply(dto)) {
            return false;
        }
        if (this.max != null && this.consumed >= this.max) {
            return false;
        }
        this.consumed++;
        return true;
    }

    public boolean isSatisfied() {
        return this.min == null || this.consumed >= this.min;
    }

    @Override
    public String toString() {
        return "Allowed{" +
                "roles=" + roles +
                ", max=" + max +
                ", consumed=" + consumed +
                ", min=" + min +
                ", types=" + types +
                '}';
    }

    @Override
    public boolean apply(@Nullable TypedContentStructureContent csc) {
        if (csc == null) {
            return false;
        }
        if (!this.roles.isEmpty() && !this.roles.contains(csc.getRole())) {
            return false;
        }
        if (!this.types.isEmpty() && !this.types.contains(csc.getContentType())) {
            return false;
        }
        if (this.and != null && !this.and.apply(csc)) {
            return false;
        }
        return true;
    }
}
