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

package fi.vm.sade.viestintapalvelu.letter.impl;

import javax.ws.rs.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.dao.LetterBatchDAO;
import fi.vm.sade.viestintapalvelu.letter.DokumenttiIdProvider;
import fi.vm.sade.viestintapalvelu.model.LetterBatch;

/**
 * User: ratamaa
 * Date: 9.10.2014
 * Time: 13:33
 */
@Component
public class DokumenttiIdProviderImpl implements DokumenttiIdProvider {
    // SHA-1, internally calls MessageDigest.getInstance(algorithm) for each encodePassword call (thus thread-safe):
    private ShaPasswordEncoder encoder = new ShaPasswordEncoder();
    @Autowired
    private LetterBatchDAO letterBatchDAO;

    @Value("${viestintapalvelu.dokumentti.id.salt}")
    private String dokumenttiIdSalt;

    @Override
    public String generateDocumentIdForLetterBatchId(long id, String prefix) {
        return prefix+id+"-"+generateHash(prefix, id);
    }

    @Override
    public long parseLetterBatchIdByDokumenttiId(String idStr, String prefix) {
        // Expect format: <prefix>-id-<HASH(suola+prefix+id+tallentaja-oid)>
        if (!idStr.startsWith(prefix)) {
            throw new NotFoundException();
        }
        String rest = idStr.substring(prefix.length());
        String[] prts = rest.split("-");
        if (prts.length != 2) {
            throw new NotFoundException();
        }
        Long id = Long.parseLong(prts[0]);
        String hash = prts[1];
        String generedHash = generateHash(prefix, id);
        if (!hash.equals(generedHash)) {
            throw new NotFoundException();
        }
        return id;
    }

    private String generateHash(String prefix, long id) {
        LetterBatch batch = letterBatchDAO.read(id);
        if (batch == null) {
            throw new NotFoundException();
        }
        return encoder.encodePassword(dokumenttiIdSalt + prefix + id + "-" + batch.getTimestamp().getTime(), null);
    }

    public void setLetterBatchDAO(LetterBatchDAO letterBatchDAO) {
        this.letterBatchDAO = letterBatchDAO;
    }
    
    public void setEncoder(ShaPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public void setDokumenttiIdSalt(String dokumenttiIdSalt) {
        this.dokumenttiIdSalt = dokumenttiIdSalt;
    }
}
