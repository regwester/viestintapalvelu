package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name="raportoitavaliite")
@Entity
public class ReportedAttachment extends BaseEntity {
	private static final long serialVersionUID = -1443213045409858837L;

	@Column(name="liitetiedoston_nimi", nullable=false)
	private String attachmentName;
	
	@Column(name="liitetiedosto")
	private byte[] attachment;
	
	@Column(name="sisaltotyyppi", nullable=false)
	private String contentType;
	
	@Column(name="aikaleima", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public String getAttachmentName() {
		return attachmentName;
	}

	public void setAttachmentName(String attachmentName) {
		this.attachmentName = attachmentName;
	}

	public byte[] getAttachment() {
		return attachment;
	}

	public void setAttachment(byte[] attachment) {
		this.attachment = attachment;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
