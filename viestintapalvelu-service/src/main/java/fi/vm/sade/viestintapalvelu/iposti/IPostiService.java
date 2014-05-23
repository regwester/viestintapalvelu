package fi.vm.sade.viestintapalvelu.iposti;

import java.util.List;

import fi.vm.sade.viestintapalvelu.model.IPosti;

public interface IPostiService {

    public List<IPosti> findUnsent();

    public IPosti findById(Long ipostiId);

    void update(IPosti iposti);

}
