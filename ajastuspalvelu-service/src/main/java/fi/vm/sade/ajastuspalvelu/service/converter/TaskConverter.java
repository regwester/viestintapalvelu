package fi.vm.sade.ajastuspalvelu.service.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.dto.TaskListDto;

@Component
public class TaskConverter {

    public TaskListDto convert(Task model) {
        return new TaskListDto(model.getId(), model.getName());
    }

}
