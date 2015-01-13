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
package fi.vm.sade.viestintapalvelu.structure.dto.constraint;

import java.util.Collection;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.TypedContentStructure;
import fi.vm.sade.viestintapalvelu.structure.dto.TypedContentStructureContent;

/**
 * User: ratamaa
 * Date: 11.11.2014
 * Time: 15:41
 */
public class ContentStructureValidator implements ConstraintValidator<ValidContentStructure,TypedContentStructure> {
    private static final Logger logger = LoggerFactory.getLogger(ContentStructureValidator.class);

    @Override
    public void initialize(ValidContentStructure constraintAnnotation) {
    }

    @Override
    public boolean isValid(TypedContentStructure value, ConstraintValidatorContext context) {
        switch (value.getType()) {
            case letter:        return allow(value.getType(), value.getContents(),
                    where(ContentRole.body).type(ContentType.html).min(1)
            );
            case email:         return allow(value.getType(), value.getContents(),
                    where(ContentRole.header).type(ContentType.plain).min(1).max(1),
                    where(ContentRole.body).type(ContentType.html).min(1).max(1),
                    where(ContentRole.body).type(ContentType.plain).max(1),
                    where(ContentRole.attachment).type(ContentType.html)
            );
            case asiointitili:  return allow(value.getType(), value.getContents(),
                    where(ContentRole.header).type(ContentType.plain).min(1).max(1),
                    where(ContentRole.body).type(ContentType.plain).min(1).max(1),
                    where(ContentRole.attachment).type(ContentType.html),
                    where(ContentRole.sms).type(ContentType.plain).max(1)
            );
            default: throw new IllegalStateException("Unimplemented ContentStructure.type="+value.getType()
                    +" in ContentStructureValidator");
        }
    }

    private boolean allow(ContentStructureType type, Collection<? extends TypedContentStructureContent> contents, ContentStructurePredicate... contentStructurePredicates) {
        dtos: for (TypedContentStructureContent dto : contents) {
            for (ContentStructurePredicate predicate : contentStructurePredicates) {
                if (predicate.consumes(dto)) {
                    continue dtos;
                }
            }
            logger.error("Content {} in ContentStructureType={} is not allowed content in structure.", dto, type);
            return false;
        }
        for (ContentStructurePredicate predicate : contentStructurePredicates) {
            if (!predicate.isSatisfied()) {
                logger.error("Rule {} in ContentStructureType={} is not satisfied by specified contents.", predicate,  type);
                return false;
            }
        }
        return true;
    }

    protected ContentStructurePredicate where(ContentRole ...roles) {
        return new ContentStructurePredicate(roles);
    }

}
