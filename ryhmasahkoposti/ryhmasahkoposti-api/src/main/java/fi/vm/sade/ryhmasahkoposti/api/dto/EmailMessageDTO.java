package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.Date;

public class EmailMessageDTO extends EmailMessage {
	private Long messageID;
	private Date startTime;
	private Date endTime;

	public Long getMessageID() {
		return messageID;
	}

	public void setMessageID(Long messageID) {
		this.messageID = messageID;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
}
