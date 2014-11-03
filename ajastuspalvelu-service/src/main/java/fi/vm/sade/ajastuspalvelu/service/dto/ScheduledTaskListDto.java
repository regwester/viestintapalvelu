package fi.vm.sade.ajastuspalvelu.service.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledTaskListDto implements Serializable {
    private static final long serialVersionUID = -5059282779150572999L;

    private final Long id;

    private final String taskName;

    private final String hakuOid;

    private final DateTime runtimeForSingle;

    public ScheduledTaskListDto(Long id, String taskName, String hakuOid, DateTime runtimeForSingle) {
        this.id = id;
        this.taskName = taskName;
        this.hakuOid = hakuOid;
        this.runtimeForSingle = runtimeForSingle;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hakuOid == null) ? 0 : hakuOid.hashCode());
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((runtimeForSingle == null) ? 0 : runtimeForSingle.hashCode());
        result = prime * result + ((taskName == null) ? 0 : taskName.hashCode());
        return result;
    }

    public Long getId() {
        return id;
    }

    public String getTaskName() {
        return taskName;
    }

    public String getHakuOid() {
        return hakuOid;
    }

    public DateTime getRuntimeForSingle() {
        return runtimeForSingle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScheduledTaskListDto other = (ScheduledTaskListDto) obj;
        if (hakuOid == null) {
            if (other.hakuOid != null)
                return false;
        } else if (!hakuOid.equals(other.hakuOid))
            return false;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (runtimeForSingle == null) {
            if (other.runtimeForSingle != null)
                return false;
        } else if (!runtimeForSingle.equals(other.runtimeForSingle))
            return false;
        if (taskName == null) {
            if (other.taskName != null)
                return false;
        } else if (!taskName.equals(other.taskName))
            return false;
        return true;
    }

}
