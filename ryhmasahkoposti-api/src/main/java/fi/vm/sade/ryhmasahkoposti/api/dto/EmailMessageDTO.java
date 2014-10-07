package fi.vm.sade.ryhmasahkoposti.api.dto;

import java.util.Date;
import java.util.List;

public class EmailMessageDTO extends EmailMessage {
    private Long messageID;
    private Date startTime;
    private Date endTime;
    private boolean isVirusChecked;
    private boolean isInfected;
    private String type;
    private List<ReplacementDTO> messageReplacements;

    public boolean isVirusChecked() {
        return isVirusChecked;
    }

    public void setVirusChecked(boolean virusChecked) {
        this.isVirusChecked = virusChecked;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean isInfected) {
        this.isInfected = isInfected;
    }

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

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the messageReplacements
     */
    public List<ReplacementDTO> getMessageReplacements() {
        return messageReplacements;
    }

    /**
     * @param messageReplacements the messageReplacements to set
     */
    public void setMessageReplacements(List<ReplacementDTO> messageReplacements) {
        this.messageReplacements = messageReplacements;
    }

}
