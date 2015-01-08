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

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * User: ratamaa
 * Date: 6.10.2014
 * Time: 17:19
 */
@Entity
@DiscriminatorValue("GENERAL")
public class LetterBatchGeneralProcessingError extends LetterBatchProcessingError {
    private static final long serialVersionUID = -1816998415185592381L;
}
