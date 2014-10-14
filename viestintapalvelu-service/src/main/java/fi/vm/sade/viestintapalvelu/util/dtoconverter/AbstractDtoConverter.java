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

package fi.vm.sade.viestintapalvelu.util.dtoconverter;

import java.lang.reflect.InvocationTargetException;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;

import org.hibernate.Session;
import org.hibernate.internal.util.StringHelper;

import com.google.common.base.Function;
import com.google.common.collect.Collections2;
import com.wordnik.swagger.annotations.ApiModel;

import fi.ratamaa.dtoconverter.AbstractAllInclusiveDtoConveter;
import fi.ratamaa.dtoconverter.ConversionCall;
import fi.ratamaa.dtoconverter.codebuilding.CodeBuilder;
import fi.ratamaa.dtoconverter.mapper.ConversionScope;
import fi.ratamaa.dtoconverter.reflection.PropertyConversionContext;
import fi.ratamaa.dtoconverter.typeconverter.TypeConversionContainer;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 9:48
 */
public class AbstractDtoConverter extends AbstractAllInclusiveDtoConveter {
    private EntityManager entityManager;

    @Override
    protected void registerConverters(TypeConversionContainer conversions) {
        super.registerConverters(conversions);
        conversions.add(new XmlDateTypeConversions());
    }

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Session getCurrentSession() {
        return entityManager.unwrap(Session.class);
    }

    @Override
    protected CodeBuilder createCodeBuilder() {
        return null;
    }

    @Override
    protected <From, To> To convertValue(From from, To to, ConversionCall call) {
        try {
            return super.convertValue(from, to, call);
        } catch(RuntimeException e) {
            clearException(e);
            throw e;
        }
    }

    @Override
    protected <From, To> To convertValue(From from, To to, Object... params) {
        try {
            return super.convertValue(from, to, params);
        } catch(RuntimeException e) {
            clearException(e);
            throw e;
        }
    }

    @Override
    protected <From, To> To convertValue(From from, To to, PropertyConversionContext parentConversion, ConversionCall call) {
        try {
            return super.convertValue(from, to, parentConversion, call);
        } catch(RuntimeException e) {
            clearException(e);
            throw e;
        }
    }

    @Override
    protected <From, To> To convertValue(From from, To to, ConversionScope scope, ConversionCall call) {
        try {
            return super.convertValue(from, to, scope, call);
        } catch(RuntimeException e) {
            clearException(e);
            throw e;
        }
    }

    protected void clearException(Throwable e) {
        if (e instanceof BadRequestException) {
            throw (BadRequestException) e;
        }
        if (e instanceof ConstraintViolationException) {
            throw badRequest((ConstraintViolationException) e);
        }
        if (e.getCause() != null && e.getCause() instanceof InvocationTargetException) {
            clearException(((InvocationTargetException) e.getCause()).getTargetException());
        } else if (e.getCause() != null && e.getCause() instanceof BadRequestException) {
            clearException(e.getCause());
        } else if (e.getCause() != null && e.getCause() instanceof ConstraintViolationException) {
            clearException(e.getCause());
        }
    }

    private static final Function<ConstraintViolation, String> TRANSLATOR = new Function<ConstraintViolation, String>() {
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

    protected BadRequestException badRequest(ConstraintViolationException e) {
        Response response = Response.status(Response.Status.BAD_REQUEST).entity(
            StringHelper.join(", ", Collections2.transform(e.getConstraintViolations(), TRANSLATOR).iterator())
        ).build();
        throw new BadRequestException(response, e);
    }
}
