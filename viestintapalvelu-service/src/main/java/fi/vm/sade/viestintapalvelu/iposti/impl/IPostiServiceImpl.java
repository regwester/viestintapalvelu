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
package fi.vm.sade.viestintapalvelu.iposti.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fi.vm.sade.viestintapalvelu.dao.IPostiDAO;
import fi.vm.sade.viestintapalvelu.iposti.IPostiService;
import fi.vm.sade.viestintapalvelu.model.IPosti;

@Service
@Transactional
public class IPostiServiceImpl implements IPostiService {

    private IPostiDAO iPostiDAO;

    @Autowired
    public IPostiServiceImpl(IPostiDAO iPostiDao) {
        this.iPostiDAO = iPostiDao;
    }

    @Override
    @Transactional(readOnly = true)
    public IPosti findBatchById(Long ipostiId) {
        List<IPosti> results = iPostiDAO.findBy("id", ipostiId);
        return (results != null && results.size() > 0) ? results.get(0) : null;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IPosti> findUnsent() {
        return iPostiDAO.findUnSent();
    }

    @Override
    public void update(IPosti iposti) {
        iPostiDAO.update(iposti);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IPosti> findMailById(Long mailId) {
        return iPostiDAO.findByLetterBatchId(mailId);
    }

    @Override
    public boolean markAsSent(Long id, Long version) {
        return iPostiDAO.markAsSent(id, version) == 1;
    }
}
