package fi.vm.sade.viestintapalvelu.infrastructure;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import fi.vm.sade.viestintapalvelu.domain.address.HasPostalAddress;
import fi.vm.sade.viestintapalvelu.domain.address.PostalAddress;

public class PdfDocument implements HasPostalAddress {
	private PostalAddress postalAddress;
	private byte[] frontPage;
	private byte[] attachment;

	public PdfDocument(PostalAddress postalAddress, byte[] frontPage,
			byte[] attachment) {
		this.postalAddress = postalAddress;
		this.frontPage = frontPage;
		this.attachment = attachment;
	}

	@Override
	public PostalAddress postalAddress() {
		return postalAddress;
	}

	public InputStream getFrontPage() {
		return new ByteArrayInputStream(frontPage);
	}

	public InputStream getAttachment() {
		return new ByteArrayInputStream(attachment);
	}
}
