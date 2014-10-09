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

package fi.vm.sade.viestintapalvelu.util;

import fi.vm.sade.viestintapalvelu.letter.DokumenttiIdProvider;

/**
 * User: ratamaa
 * Date: 9.10.2014
 * Time: 13:45
 */
public class DummyDokumenttiIdProvder implements DokumenttiIdProvider {

    @Override
    public String generateDocumentIdForLetterBatchId(long id, String prefix) {
        return prefix+id+"-dummy";
    }

    @Override
    public String generateDocumentIdForLetterBatchId(long id, String prefix, String oid) {
        return generateDocumentIdForLetterBatchId(id, prefix);
    }

    @Override
    public long parseLetterBatchIdByDokumenttiId(String idStr, String prefix, String oid) {
        return parseLetterBatchIdByDokumenttiId(idStr, prefix, oid);
    }

    @Override
    public long parseLetterBatchIdByDokumenttiId(String idStr, String prefix) {
        return Long.parseLong(idStr.substring(prefix.length()).split("-")[0]);
    }

}