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

package fi.vm.sade.viestintapalvelu.letter;

/**
 * User: ratamaa
 * Date: 9.10.2014
 * Time: 13:40
 */
public interface DokumenttiIdProvider {
    /**
     * @param id of LetterBatch to make document id from
     * @param prefix for the id
     * @return the genrated id
     */
    String generateDocumentIdForLetterBatchId(long id, String prefix);

    /**
     * @param id of LetterBatch to make document id from
     * @param prefix for the id
     * @param oid Henkilo OID of the user
     * @return the genrated id
     */
    String generateDocumentIdForLetterBatchId(long id, String prefix, String oid);

    /**
     * @param idStr id string to parse
     * @param prefix the prefix expected
     * @param oid the expected Henkilo OID of the current user
     * @return the valid id of a LetterBatch
     * @throws javax.ws.rs.NotFoundException if ID was invalid
     */
    long parseLetterBatchIdByDokumenttiId(String idStr, String prefix, String oid);

    /**
     * @param idStr id string to parse
     * @param prefix the prefix expected
     * @return the valid id of a LetterBatch
     * @throws javax.ws.rs.NotFoundException if ID was invalid
     */
    long parseLetterBatchIdByDokumenttiId(String idStr, String prefix);
}
