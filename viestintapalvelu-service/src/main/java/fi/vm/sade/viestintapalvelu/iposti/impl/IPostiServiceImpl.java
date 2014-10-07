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
        return iPostiDAO.findMailById(mailId);
    }
}
