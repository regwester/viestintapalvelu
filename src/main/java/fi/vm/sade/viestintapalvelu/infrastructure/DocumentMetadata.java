package fi.vm.sade.viestintapalvelu.infrastructure;

import fi.vm.sade.viestintapalvelu.domain.address.HasPostalAddress;
import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;

public class DocumentMetadata implements HasPostalAddress {
	private PostalAddress postalAddress;
	private int startPage;
	private int pages;

	public DocumentMetadata(PostalAddress postalAddress, int startPage,
			int pages) {
		this.postalAddress = postalAddress;
		this.startPage = startPage;
		this.pages = pages;
	}

	@Override
	public PostalAddress getPostalAddress() {
		return postalAddress;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getPages() {
		return pages;
	}
}
