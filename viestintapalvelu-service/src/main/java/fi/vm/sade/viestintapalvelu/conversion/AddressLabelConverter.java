package fi.vm.sade.viestintapalvelu.conversion;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverAddress;

public class AddressLabelConverter {
    
    public static AddressLabel convert(LetterReceiverAddress address) {
        return new AddressLabel(address.getFirstName(), address.getLastName(),
                address.getAddressline(), address.getAddressline2(), address.getAddressline3(), 
                address.getPostalCode(), address.getCity(),
                address.getRegion(), address.getCountry(), address.getCountryCode());
    }
}
