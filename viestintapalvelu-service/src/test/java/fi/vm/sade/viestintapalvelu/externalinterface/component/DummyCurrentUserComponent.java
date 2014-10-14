package fi.vm.sade.viestintapalvelu.externalinterface.component;

import java.util.List;

import org.springframework.stereotype.Component;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.viestintapalvelu.testdata.DocumentProviderTestData;

@Component
public class DummyCurrentUserComponent implements CurrentUserComponent {

    @Override
    public Henkilo getCurrentUser() {
        return DocumentProviderTestData.getHenkilo();
    }

    @Override
    public List<OrganisaatioHenkilo> getCurrentUserOrganizations() {
        return null;
    }

}
