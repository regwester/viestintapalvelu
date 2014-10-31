package fi.vm.sade.ajastuspalvelu.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("/{scheduledtaskid}")
    public Response getScheduledTask(@PathParam("scheduledtaskid") Long scheduledTaskId) {
        ScheduledTaskDto dto = service.findById(scheduledTaskId);
        return Response.status(Status.OK).entity(dto).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("/list")
    public Response getScheduledTasks() {
        List<ScheduledTaskDto> dtos = service.list();
        return Response.status(Status.OK).entity(dtos).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public Response insertScheduledTask(ScheduledTaskDto dto) {
        service.insert(dto);
        return Response.status(Status.OK).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON_VALUE)
    @Produces(MediaType.TEXT_PLAIN_VALUE)
    public Response update(ScheduledTaskDto dto) {
        service.update(dto);
        return Response.status(Status.OK).build();
    }
    
    @PUT
    @Path("/close/{scheduledtaskid}")
    @Produces(MediaType.TEXT_PLAIN_VALUE)
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
