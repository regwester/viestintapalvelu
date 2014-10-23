package fi.vm.sade.viestintapalvelu.iposti;

import java.util.List;

import fi.vm.sade.viestintapalvelu.model.IPosti;

public interface IPostiService {

    public List<IPosti> findUnsent();

    public IPosti findBatchById(Long ipostiId);
    
    public List<IPosti> findMailById(Long mailId);

    void update(IPosti iposti);
    
    boolean markAsSent(IPosti iposti);

}
