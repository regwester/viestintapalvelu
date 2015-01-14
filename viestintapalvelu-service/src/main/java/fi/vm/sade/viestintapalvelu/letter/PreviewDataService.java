/*
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
 */

package fi.vm.sade.viestintapalvelu.letter;

import com.lowagie.text.DocumentException;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.template.Template;

import java.io.IOException;

/**
 * Created by jonimake on 12.1.2015.
 */
public interface PreviewDataService {
    Letter getLetter(Template template);

    fi.vm.sade.viestintapalvelu.model.LetterBatch getLetterBatch(int numLetters, Template template, LetterReceivers letterReceivers, String applicationPeriod);

    LetterReceivers getLetterReceivers(Template template, String applicationPeriod);

    byte[] getPreviewPdf(Template template, String applicationPeriod) throws IOException, DocumentException;

    String getEmailPreview(Template template, String applicationPeriod);
}
