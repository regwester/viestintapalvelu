package fi.vm.sade.viestintapalvelu.document;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import fi.vm.sade.viestintapalvelu.address.AddressLabel;

public class PdfDocument {
	private AddressLabel addressLabel;
	private byte[] frontPage;
	private byte[] attachment;

	public PdfDocument(AddressLabel addressLabel, byte[] frontPage,
			byte[] attachment) {
		this.addressLabel = addressLabel;
		this.frontPage = frontPage;
		this.attachment = attachment;
	}

	public AddressLabel getAddressLabel() {
		return addressLabel;
	}

	public InputStream getFrontPage() {
		return new ByteArrayInputStream(frontPage);
	}

	public InputStream getAttachment() {
		return new ByteArrayInputStream(attachment);
	}
}
