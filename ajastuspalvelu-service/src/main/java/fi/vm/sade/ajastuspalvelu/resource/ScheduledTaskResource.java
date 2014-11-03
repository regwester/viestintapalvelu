package fi.vm.sade.ajastuspalvelu.resource;

import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import fi.vm.sade.ajastuspalvelu.service.ScheduledTaskService;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskListDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskModifyDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskSaveDto;
import fi.vm.sade.viestintapalvelu.common.util.BeanValidator;

@PreAuthorize("isAuthenticated()")
@Path("scheduledtask")
@Api(value = "scheduledtask", description = "Ajastetut tehtävät")
public class ScheduledTaskResource {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(ScheduledTaskResource.class); 
    
    @Autowired
    private ScheduledTaskService service;

    @Autowired
    private BeanValidator beanValidator;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("/{scheduledtaskid}")
    @ApiOperation(value = "Hakee muokattavan ajastuksen tiedot",response = ScheduledTaskModifyDto.class)
    public Response getScheduledTask(@PathParam("scheduledtaskid") Long scheduledTaskId) {
        ScheduledTaskModifyDto dto = service.findById(scheduledTaskId);
        return Response.status(Status.OK).entity(dto).build();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @Path("/list")
    @ApiOperation(value = "Hakee kaikki ajastetut tehtävät",responseContainer = "list", response = ScheduledTaskListDto.class)
    public Response getScheduledTasks() {
        List<ScheduledTaskListDto> dtos = service.list();
        return Response.status(Status.OK).entity(dtos).build();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON_VALUE+"; charset=UTF-8")
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Tallentaa ajastetun tehtävän", response = ScheduledTaskListDto.class)
    public Response insertScheduledTask(ScheduledTaskSaveDto dto) throws SchedulerException {
        beanValidator.validate(dto);
        ScheduledTaskListDto inserted = service.insert(dto);
        return Response.ok(inserted).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON_VALUE+"; charset=UTF-8")
    @ApiOperation("Päivittää ajastetun tehtävän tiedot")
    public Response update(ScheduledTaskModifyDto dto) throws SchedulerException {
        beanValidator.validate(dto);
        service.update(dto);
        return Response.status(Status.OK).build();
    }
    
    @PUT
    @Path("/close/{scheduledtaskid}")
    @ApiOperation("Poistaa ajastetun tehtävän (ajastuksen päättäminen)")
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
