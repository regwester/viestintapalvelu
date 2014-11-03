package fi.vm.sade.ajastuspalvelu.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import fi.vm.sade.ajastuspalvelu.service.TaskService;
import fi.vm.sade.ajastuspalvelu.service.dto.TaskListDto;

@PreAuthorize("isAuthenticated()")
@Path("task")
public class TaskResource {

    @Autowired
    private TaskService service;
    
    @GET
    public Response getTasks() {
        List<TaskListDto> tasks = service.list();
        return Response.status(Status.OK).entity(tasks).build();
    }
    
}
