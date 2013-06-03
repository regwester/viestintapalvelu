package fi.vm.sade.viestintapalvelu;

import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;
import fi.vm.sade.viestintapalvelu.test.stub.PostalAddressStub;

class Fixture {
	static PostalAddress address = new PostalAddressStub("Åle", "Öistämö",
			"Brännkyrksgatan 177 B 149", "Södermalm", "13", "65330",
			"Stockholm", "SL", "Sweden", "SE");
	static PostalAddress addressWithSpecialCharacters = new PostalAddressStub(
			"Åle &", "Öistämö &", "Brännkyrksgatan & 177 B 149", "Södermalm &",
			"13&", "65330 &", "Stockholm &", "SL&", "Sweden&", "SE");
}
