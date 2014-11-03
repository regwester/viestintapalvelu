package fi.vm.sade.ryhmasahkoposti.api.dto;

public class EmailSendId {
	private String id = "";

    public EmailSendId() {
    }

    public EmailSendId(String id) {
		super();
		this.id = id;
	}

	public String getId() {
		return id;
	}

    public void setId(String id) {
        this.id = id;
    }
}
