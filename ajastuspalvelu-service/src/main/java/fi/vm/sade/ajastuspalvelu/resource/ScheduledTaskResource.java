package fi.vm.sade.ajastuspalvelu.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import fi.vm.sade.ajastuspalvelu.service.ScheduledTaskService;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskDto;

@PreAuthorize("isAuthenticated()")
@Path("scheduledtask")
public class ScheduledTaskResource {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskResource.class); 
    
    @Autowired
    private ScheduledTaskService service;
    
    @GET
    @Path("/list")
    public Response getScheduledTasks() {
        List<ScheduledTaskDto> dto = service.list();
        return Response.status(Status.OK).entity(dto).build();
    }
    
    @PUT
    public Response update(ScheduledTaskDto dto) {
        service.update(dto);
        return Response.status(Status.OK).build();
    }
    
    @PUT
    @Path("/close/{scheduledtaskid}")
    public Response close(@PathParam("scheduledtaskid") Long id) {
        try {
            service.remove(id);
        } catch (SchedulerException e) {
            LOGGER.error("Exception caught while closing ScheduledTask with id " + id, e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
        return Response.status(Status.OK).build();
    }
}
