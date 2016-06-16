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
import java.util.List;

/**
* User: ratamaa
* Date: 3.10.2014
* Time: 13:36
*/
public class LetterReceiverProcessable implements Processable {
    private static final long serialVersionUID = 1L;
    private Long id;

    public LetterReceiverProcessable(Long id) {
        this.id = id;
    }

    public static List<LetterReceiverProcessable> forIds(List<Long> ids) {
        List<LetterReceiverProcessable> processables = new ArrayList<>();
        for (Long id : ids) {
            processables.add(new LetterReceiverProcessable(id));
        }
        return processables;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "LetterBatchReceiver="+id;
    }
}
