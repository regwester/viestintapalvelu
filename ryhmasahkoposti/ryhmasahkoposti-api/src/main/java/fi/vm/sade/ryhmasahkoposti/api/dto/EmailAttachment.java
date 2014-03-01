package fi.vm.sade.ryhmasahkoposti.api.dto;

import javax.activation.DataSource;
import javax.activation.FileDataSource;

public class EmailAttachment {

	private byte[] data;
	private String name;
	private String contentType;

	public EmailAttachment() {
	}

	public EmailAttachment(String attachmentFile) {
		name = attachmentFile;
		DataSource ds = new FileDataSource(attachmentFile);
		contentType = ds.getContentType();
	}

	public String getName() {
		return name;
	}

	public String getContentType() {
		return contentType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "EmailAttachment [" + "name=" + name + ", contentType="
				+ contentType + "]";
	}
}
