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

package fi.vm.sade.viestintapalvelu.common.util.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import org.hibernate.internal.util.StringHelper;
import org.springframework.stereotype.Component;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.wordnik.swagger.annotations.ApiModel;

import fi.vm.sade.viestintapalvelu.common.util.BeanValidator;

/**
 * User: ratamaa
 * Date: 15.10.2014
 * Time: 7:58
 */
@Component
public class BeanValidatorImpl implements BeanValidator {
    private ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    private static final Function<ConstraintViolation, String> VIOLATION_TRANSLATOR =
            new Function<ConstraintViolation, String>() {
        @Nullable
        @Override
        public String apply(@Nullable ConstraintViolation input) {
            if (input == null) {
                return "";
            }
            String clzName = input.getRootBeanClass().getSimpleName();
            if (input.getRootBeanClass().isAnnotationPresent(ApiModel.class)) {
                ApiModel model = (ApiModel) input.getRootBeanClass().getAnnotation(ApiModel.class);
                clzName += " ("+ model.value() + ") ";
            } else {
                clzName += ".";
            }
            return input.getMessage() +" "+clzName
                    + input.getPropertyPath().toString()
                    + " " + input.getInvalidValue();
        }
    };

    @Override
    public<T> void validate(T bean) throws BadRequestException {
        Set<ConstraintViolation<T>> violations = validatorFactory.getValidator().validate(bean);
        if (!violations.isEmpty()) {
            Collection<String> errors = Collections2.transform(violations, VIOLATION_TRANSLATOR);
            throw badRequest(errors.toArray(new String[errors.size()]));
        }
    }

    public static BadRequestException badRequest(String... errors) {
        Map<String,Object> result = new HashMap<String, Object>();
        result.put("status", 400);
        result.put("description", StringHelper.join(", ", errors));
        result.put("errors", errors);
        Response response = Response.status(Response.Status.BAD_REQUEST).entity(result).build();
        return new BadRequestException(response);
    }

}
