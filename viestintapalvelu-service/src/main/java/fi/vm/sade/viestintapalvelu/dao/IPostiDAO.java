package fi.vm.sade.viestintapalvelu.dao;

import java.util.List;

import fi.vm.sade.generic.dao.JpaDAO;
import fi.vm.sade.viestintapalvelu.model.IPosti;

public interface IPostiDAO extends JpaDAO<IPosti, Long>{

    public List<IPosti> findUnSent();

    public List<IPosti> findMailById(Long id);

}
