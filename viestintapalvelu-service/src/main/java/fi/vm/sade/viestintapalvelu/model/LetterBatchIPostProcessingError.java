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

package fi.vm.sade.viestintapalvelu.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;

/**
 * User: ratamaa
 * Date: 6.10.2014
 * Time: 12:32
 */
@DiscriminatorValue("IPOSTI")
public class LetterBatchIPostProcessingError extends LetterBatchProcessingError {
    private static final long serialVersionUID = -721331642198243867L;

    @Column(name="iposti_order_number", nullable = false)
    private int orderNumber;

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }
}