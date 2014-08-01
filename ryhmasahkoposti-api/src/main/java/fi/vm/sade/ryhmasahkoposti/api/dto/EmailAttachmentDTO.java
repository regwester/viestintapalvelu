package fi.vm.sade.ryhmasahkoposti.api.dto;

public class EmailAttachmentDTO extends EmailAttachment {

	private Long attachmentID;

	public void setAttachmentID(Long attachmentID) {
		this.attachmentID = attachmentID;
	}
	
	public Long getAttachmentID() {
		return this.attachmentID;
	}
}
