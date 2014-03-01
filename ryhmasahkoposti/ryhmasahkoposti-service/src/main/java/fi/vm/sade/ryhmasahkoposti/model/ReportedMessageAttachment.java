package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name="raportoitavaviesti_raportoitavaliite")
@Entity
public class ReportedMessageAttachment extends BaseEntity {
	private static final long serialVersionUID = -8639217659820696701L;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="lahetettyviesti_id")
	private ReportedMessage reportedMessage;
	
	@Column(name="raportoitavaliite_id")
	private Long reportedAttachmentID;
	
	@Column(name="aikaleima", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;

	public ReportedMessage getReportedMessage() {
		return reportedMessage;
	}

	public void setReportedMessage(ReportedMessage reportedMessage) {
		this.reportedMessage = reportedMessage;
	}

	public Long getReportedAttachmentID() {
		return reportedAttachmentID;
	}

	public void setReportedAttachmentID(Long reportedAttachmentID) {
		this.reportedAttachmentID = reportedAttachmentID;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
