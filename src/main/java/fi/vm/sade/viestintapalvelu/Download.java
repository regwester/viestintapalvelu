package fi.vm.sade.viestintapalvelu;

public class Download {
	private final String contentType;
	private final String filename;
	private final byte[] binaryDocument;

	public Download(String contentType, String filename, byte[] binaryDocument) {
		this.contentType = contentType;
		this.filename = filename;
		this.binaryDocument = binaryDocument;
	}

	public String getFilename() {
		return filename;
	}

	public byte[] toByteArray() {
		return binaryDocument;
	}

	public String getContentType() {
		return contentType;
	}
}
