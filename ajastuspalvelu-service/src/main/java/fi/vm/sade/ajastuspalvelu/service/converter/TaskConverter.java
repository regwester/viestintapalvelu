package fi.vm.sade.ajastuspalvelu.service.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.dto.TaskDto;

@Component
public class TaskConverter implements Converter<Task, TaskDto> {

    @Override
    public Task convertToModel(TaskDto dto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TaskDto convertToDto(Task model) {
        return new TaskDto(model.getId(), model.getName());
    }

}
