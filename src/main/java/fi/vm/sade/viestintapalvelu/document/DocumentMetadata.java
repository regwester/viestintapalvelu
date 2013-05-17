package fi.vm.sade.viestintapalvelu.document;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;


public class DocumentMetadata {
	private AddressLabel addressLabel;
	private int startPage;
	private int endPage;

	public DocumentMetadata(AddressLabel addressLabel, int startPage, int endPage) {
		this.addressLabel = addressLabel;
		this.startPage = startPage;
		this.endPage = endPage;
	}

	public AddressLabel getAddressLabel() {
		return addressLabel;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}
}
