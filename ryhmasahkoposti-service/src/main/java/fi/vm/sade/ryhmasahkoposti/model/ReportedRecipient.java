package fi.vm.sade.ryhmasahkoposti.model;

import fi.vm.sade.generic.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

@Table(name = "raportoitavavastaanottaja")
@Entity
public class ReportedRecipient extends BaseEntity {
    private static final long serialVersionUID = -4957288730521500299L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lahetettyviesti_id")
    private ReportedMessage reportedMessage;

    @Column(name = "vastaanottajan_oid", nullable = false)
    private String recipientOid;

    @Column(name = "vastaanottajan_oid_tyyppi", nullable = false)
    private String recipientOidType;

    @Column(name = "henkilotunnus", nullable = false)
    private String socialSecurityID;

    @Column(name = "vastaanottajan_sahkopostiosoite", nullable = false)
    private String recipientEmail;

    @Column(name = "kielikoodi", nullable = false)
    private String languageCode;

    @Column(name = "hakunimi", nullable = false)
    private String searchName;

    @Column(name = "lahetysalkoi", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendingStarted;

    @Column(name = "lahetyspaattyi", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date sendingEnded;

    @Column(name = "lahetysonnistui", nullable = true)
    private String sendingSuccesful;

    @Column(name = "epaonnistumisensyy", nullable = true)
    private String failureReason;

    @Column(name = "aikaleima", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Column(name = "henkilotiedot_haettu", nullable = false)
    private Boolean detailsRetrieved;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jono", nullable = true)
    private SendQueue queue;

    public ReportedMessage getReportedMessage() {
        return reportedMessage;
    }

    public void setReportedMessage(ReportedMessage reportedMessage) {
        this.reportedMessage = reportedMessage;
    }

    public String getRecipientOid() {
        return recipientOid;
    }

    public void setRecipientOid(String recipientOid) {
        this.recipientOid = recipientOid;
    }

    public String getRecipientOidType() {
        return recipientOidType;
    }

    public void setRecipientOidType(String recipientOidType) {
        this.recipientOidType = recipientOidType;
    }

    public String getSocialSecurityID() {
        return socialSecurityID;
    }

    public void setSocialSecurityID(String socialSecurityID) {
        this.socialSecurityID = socialSecurityID;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
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

    public String getSendingSuccesful() {
        return sendingSuccesful;
    }

    public void setSendingSuccesful(String sendingSuccesful) {
        this.sendingSuccesful = sendingSuccesful;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getDetailsRetrieved() {
        return detailsRetrieved;
    }

    public void setDetailsRetrieved(Boolean detailsRetrieved) {
        this.detailsRetrieved = detailsRetrieved;
    }

    public SendQueue getQueue() {
        return queue;
    }

    public void setQueue(SendQueue queue) {
        this.queue = queue;
    }
}
