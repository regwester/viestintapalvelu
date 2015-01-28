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
package fi.vm.sade.viestintapalvelu.letter.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * User: ratamaa
 * Date: 3.10.2014
 * Time: 13:48
 */
public class IPostiProcessable implements Processable {
    private static final long serialVersionUID = 1L;
    
    private final long letterBatchId;
    private final int orderNumber;
    private final List<Long> letterReceiverIds = new ArrayList<Long>();

    public IPostiProcessable(long letterBatchId, int orderNumber) {
        this.letterBatchId = letterBatchId;
        this.orderNumber = orderNumber;
    }

    public void addLetterReceiverIds(Collection<Long> letterReceivers) {
        this.letterReceiverIds.addAll(letterReceivers);
    }

    public List<Long> getLetterReceiverIds() {
        return letterReceiverIds;
    }

    public long getLetterBatchId() {
        return letterBatchId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    @Override
    public String toString() {
        return "IPosti "+orderNumber + " for letterBatch="+letterBatchId;
    }
}
