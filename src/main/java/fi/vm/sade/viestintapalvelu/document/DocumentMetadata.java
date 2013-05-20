package fi.vm.sade.viestintapalvelu.document;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;


public class DocumentMetadata {
	private AddressLabel addressLabel;
	private int startPage;
	private int pages;

	public DocumentMetadata(AddressLabel addressLabel, int startPage, int pages) {
		this.addressLabel = addressLabel;
		this.startPage = startPage;
		this.pages = pages;
	}

	public AddressLabel getAddressLabel() {
		return addressLabel;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getPages() {
		return pages;
	}
}
