package fi.vm.sade.ajastuspalvelu.resource;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.security.access.prepost.PreAuthorize;

import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskDto;

@PreAuthorize("isAuthenticated()")
@Path("sheduledtask")
public class ScheduledTaskResource {
    
    @GET
    @Path("/list")
    public Response getScheduledTasks() {
        return null;
    }
    
    @PUT
    public Response update(ScheduledTaskDto dto) {
        return null;
    }
    
    @DELETE
    public Response delete(Long id) {
        return null;
    }
}
