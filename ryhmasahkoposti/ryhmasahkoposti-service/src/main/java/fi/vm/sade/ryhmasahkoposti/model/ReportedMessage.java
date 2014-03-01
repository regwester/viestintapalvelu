package fi.vm.sade.ryhmasahkoposti.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import fi.vm.sade.generic.model.BaseEntity;

@Table(name="raportoitavaviesti")
@Entity()
public class ReportedMessage extends BaseEntity {
	private static final long serialVersionUID = 7511140604535983187L;

	@Column(name="prosessi", nullable=false)
	private String process;
	
	@Column(name="lahettajan_oid", nullable=true)
	private String senderOid;

	@Column(name="lahettajan_oid_tyyppi", nullable=true)
	private String senderOidType;

	@Column(name="lahettajan_sahkopostiosoite", nullable=false)
	private String senderEmail;

	@Column(name="vastauksensaajan_oid", nullable=true)
	private String replyToOid;

	@Column(name="vastauksensaajan_oid_tyyppi", nullable=true)
	private String replyToOidType;

	@Column(name="vastauksensaajan_sahkopostiosoite", nullable=true)
	private String replyToEmail;

	@Column(name="aihe", nullable=false)
	private String subject;
	
	@Column(name="viesti", nullable=false)
	private String message;

	@Column(name="htmlviesti", nullable=false)
	private String htmlMessage;

	@Column(name="merkisto", nullable=false)
	private String characterSet;

	@OneToMany(mappedBy="reportedMessage", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<ReportedRecipient> reportedRecipients;
	
	@OneToMany(mappedBy="reportedMessage", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<ReportedMessageAttachment> reportedMessageAttachments;	
	
	@Column(name="lahetysalkoi", nullable=false)
	private Date sendingStarted;
	
	@Column(name="lahetyspaattyi", nullable=true)
	private Date sendingEnded;
	
	@Column(name="aikaleima", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	
	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process;
	}

	public String getSenderOid() {
		return senderOid;
	}

	public void setSenderOid(String senderOid) {
		this.senderOid = senderOid;
	}

	public String getSenderOidType() {
		return senderOidType;
	}

	public void setSenderOidType(String senderOidType) {
		this.senderOidType = senderOidType;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getReplyToOid() {
		return replyToOid;
	}

	public void setReplyToOid(String replyToOid) {
		this.replyToOid = replyToOid;
	}

	public String getReplyToOidType() {
		return replyToOidType;
	}

	public void setReplyToOidType(String replyToOidType) {
		this.replyToOidType = replyToOidType;
	}

	public String getReplyToEmail() {
		return replyToEmail;
	}

	public void setReplyToEmail(String replyToEmail) {
		this.replyToEmail = replyToEmail;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getHtmlMessage() {
		return htmlMessage;
	}

	public void setHtmlMessage(String htmlMessage) {
		this.htmlMessage = htmlMessage;
	}

	public String getCharacterSet() {
		return characterSet;
	}

	public void setCharacterSet(String characterSet) {
		this.characterSet = characterSet;
	}

	public Set<ReportedRecipient> getReportedRecipients() {
		return reportedRecipients;
	}

	public void setReportedRecipients(Set<ReportedRecipient> reportedRecipients) {
		this.reportedRecipients = reportedRecipients;
	}

	public Set<ReportedMessageAttachment> getReportedMessageAttachments() {
		return reportedMessageAttachments;
	}

	public void setReportedMessageAttachments(Set<ReportedMessageAttachment> reportedMessageAttachments) {
		this.reportedMessageAttachments = reportedMessageAttachments;
	}

	public Date getSendingStarted() {
		return sendingStarted;
	}

	public void setSendingStarted(Date sendingStarted) {
		this.sendingStarted = sendingStarted;
	}

	public Date getSendingEnded() {
		return sendingEnded;
	}

	public void setSendingEnded(Date sendingEnded) {
		this.sendingEnded = sendingEnded;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
}
