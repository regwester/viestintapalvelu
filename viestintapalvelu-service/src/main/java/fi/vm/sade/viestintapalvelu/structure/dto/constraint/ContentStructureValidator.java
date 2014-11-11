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
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureContentSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureSaveDto;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 15:41
 */
public class ContentStructureValidator implements ConstraintValidator<ValidContentStructure,ContentStructureSaveDto> {
    private static final Logger logger = LoggerFactory.getLogger(ContentStructureValidator.class);

    @Override
    public void initialize(ValidContentStructure constraintAnnotation) {
    }

    @Override
    public boolean isValid(ContentStructureSaveDto value, ConstraintValidatorContext context) {
        switch (value.getType()) {
            case letter:        return allow(value.getType(), value.getContents(),
                    where(ContentRole.body).type(ContentType.html).min(1)
            );
            case email:         return allow(value.getType(), value.getContents(),
                    where(ContentRole.body).type(ContentType.html).min(1).max(1),
                    where(ContentRole.body).type(ContentType.plain).max(1),
                    where(ContentRole.attachment).type(ContentType.html)
            );
            case asiointitili:  return allow(value.getType(), value.getContents(),
                    where(ContentRole.body).type(ContentType.plain).min(1).max(1),
                    where(ContentRole.attachment).type(ContentType.html),
                    where(ContentRole.sms).type(ContentType.plain).max(1)
            );
            default: throw new IllegalStateException("Unimplemented ContentStructure.type="+value.getType()
                    +" in ContentStructureValidator");
        }
    }

    private boolean allow(ContentStructureType type, List<ContentStructureContentSaveDto> contents, Allowed ...allowed) {
        dtos: for (ContentStructureContentSaveDto dto : contents) {
            for (Allowed rule : allowed) {
                if (rule.consumes(dto)) {
                    continue dtos;
                }
            }
            logger.error("Content {} in ContentStructureType={} is not allowed content in structure.", dto, type);
            return false;
        }
        for (Allowed rule : allowed) {
            if (!rule.isSatisfied()) {
                logger.error("Rule {} in ContentStructureType={} is not satisfied by specified contents.", rule,  type);
                return false;
            }
        }
        return true;
    }

    protected Allowed where(ContentRole ...roles) {
        return new Allowed(roles);
    }

    public static class Allowed {
        private Set<ContentRole> roles = new HashSet<ContentRole>();
        private Integer max;
        private int consumed = 0;
        private Integer min;
        private Set<ContentType> types = new HashSet<ContentType>();

        public Allowed(ContentRole... roles) {
            this.roles.addAll(Arrays.asList(roles));
        }

        public Allowed type(ContentType ...types) {
            this.types.addAll(Arrays.asList(types));
            return this;
        }

        public Allowed max(int max) {
            this.max = max;
            return this;
        }

        public Allowed min(int min) {
            this.min = min;
            return this;
        }

        public boolean consumes(ContentStructureContentSaveDto dto) {
            if (!this.roles.isEmpty() && !this.roles.contains(dto.getRole())) {
                return false;
            }
            if (!this.types.isEmpty() && !this.types.contains(dto.getContentType())) {
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
    }
}
