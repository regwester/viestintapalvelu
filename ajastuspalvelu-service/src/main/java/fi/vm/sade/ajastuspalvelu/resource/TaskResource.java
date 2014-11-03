package fi.vm.sade.ajastuspalvelu.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import fi.vm.sade.ajastuspalvelu.service.TaskService;
import fi.vm.sade.ajastuspalvelu.service.dto.TaskListDto;

@PreAuthorize("isAuthenticated()")
@Path("task")
@Api(value = "task", description = "Tehtävät")
public class TaskResource {

    @Autowired
    private TaskService service;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Listaa tehtävät", responseContainer = "list", response = TaskListDto.class)
    public Response getTasks() {
        List<TaskListDto> tasks = service.list();
        return Response.status(Status.OK).entity(tasks).build();
    }
    
}
