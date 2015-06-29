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
package fi.vm.sade.viestintapalvelu.letter;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.model.LetterBatch;
import fi.vm.sade.viestintapalvelu.model.LetterBatch.Status;

/**
 * User: ratamaa
 * Date: 6.10.2014
 * Time: 16:53
 */
@Component
public class LetterBatchStatusLegalityChecker {

    public void ensureLegalStateChange(long letterBatchId, Status status, Status newStatus) {
        if (status == newStatus) {
            return; // no state change, ok
        }
        List<Status> allowed = allowedNextStates(status);
        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException("Can not transfer LetterBatch="+letterBatchId + " status from " + status
                    + " to " + newStatus);
        }
    }

    private static List<Status> allowedNextStates(Status status) {
        switch (status) {
            case error:                         return list(); // no change allowed
            case ready:                         return list(); // no change allowed
            case created:                       return list(Status.processing,
                                                            Status.error);
            case processing:                    return list(Status.ready,
                                                            Status.waiting_for_ipost_processing,
                                                            Status.error);
            case waiting_for_ipost_processing:  return list(Status.processing_ipost,
                                                            Status.error);
            case processing_ipost:              return list(Status.ready,
                                                            Status.error);
            default: throw new IllegalStateException("Unknown status: " + status);
        }
    }

    private static List<Status> list(LetterBatch.Status... allowed) {
        return Arrays.asList(allowed);
    }
}
