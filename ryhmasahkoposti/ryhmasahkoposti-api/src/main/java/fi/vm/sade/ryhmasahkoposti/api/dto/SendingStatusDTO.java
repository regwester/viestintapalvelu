package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.io.Serializable;
import java.util.Date;

public class SendingStatusDTO implements Serializable {
	private static final long serialVersionUID = 6159889196702219857L;

	private Long messageID;
	private Date sendingStarted;
	private Date sendingEnded;
	private Long numberOfReciepients;
	private Long numberOfSuccesfulSendings;
	private Long numberOfFailedSendings;
	
	public Long getMessageID() {
		return messageID;
	}
	
	public void setMessageID(Long messageID) {
		this.messageID = messageID;
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
	
	public Long getNumberOfReciepients() {
		return numberOfReciepients;
	}
	
	public void setNumberOfReciepients(Long numberOfReciepients) {
		this.numberOfReciepients = numberOfReciepients;
	}
	
	public Long getNumberOfSuccesfulSendings() {
		return numberOfSuccesfulSendings;
	}
	
	public void setNumberOfSuccesfulSendings(Long numberOfSuccesfulSendings) {
		this.numberOfSuccesfulSendings = numberOfSuccesfulSendings;
	}
	
	public Long getNumberOfFailedSendings() {
		return numberOfFailedSendings;
	}
	
	public void setNumberOfFailedSendings(Long numberOfFailedSendings) {
		this.numberOfFailedSendings = numberOfFailedSendings;
	}
}
