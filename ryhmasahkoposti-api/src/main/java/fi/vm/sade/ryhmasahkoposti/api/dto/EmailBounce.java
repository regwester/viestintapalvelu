package fi.vm.sade.ryhmasahkoposti.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

public class EmailBounce {
    private String email;

    @JsonProperty("bounce_cnt")
    private int count;

    //@JsonProperty("last_bounce")
    //@JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    private DateTime timestamp = new DateTime();

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "EmailBounce{" +
                "email='" + email + '\'' +
                ", count=" + count +
                ", timestamp=" + timestamp +
                '}';
    }
}
