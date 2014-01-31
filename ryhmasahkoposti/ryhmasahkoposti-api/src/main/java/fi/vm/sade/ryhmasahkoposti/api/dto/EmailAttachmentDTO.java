package fi.vm.sade.ryhmasahkoposti.api.dto;

public class EmailAttachmentDTO extends EmailAttachment {

	private Long attachmentID;
	private byte[] data;
	
	public Long getAttachmentID() {
		return attachmentID;
	}
	public void setAttachmentID(Long attachmentID) {
		this.attachmentID = attachmentID;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
}
