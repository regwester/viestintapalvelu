package fi.vm.sade.ajastuspalvelu.service.dto;

import java.io.Serializable;

import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ScheduledTaskDto implements Serializable {
    
    public final Long id;
    
    public final String taskName;
    
    public final String hakuOid;
    
    public final DateTime runtimeForSingle;

    public ScheduledTaskDto(Long id, String taskName, String hakuOid, DateTime runtimeForSingle) {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ScheduledTaskDto other = (ScheduledTaskDto) obj;
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
