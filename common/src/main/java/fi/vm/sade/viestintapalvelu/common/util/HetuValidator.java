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

package fi.vm.sade.viestintapalvelu.common.util;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import fi.vm.sade.generic.common.HetuUtils;

/**
 * User: ratamaa
 * Date: 14.10.2014
 * Time: 15:42
 */
public class HetuValidator implements ConstraintValidator<ValidHetu,String> {
    @Override
    public void initialize(ValidHetu constraintAnnotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return HetuUtils.isHetuValid(value);
    }
}
