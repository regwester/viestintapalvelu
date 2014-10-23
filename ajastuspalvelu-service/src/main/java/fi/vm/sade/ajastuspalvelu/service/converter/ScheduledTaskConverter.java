package fi.vm.sade.ajastuspalvelu.service.converter;

import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskDto;

@Component("scheduledTaskConverter")
public class ScheduledTaskConverter implements Converter<ScheduledTask, ScheduledTaskDto> {

    @Override
    public ScheduledTask convertToModel(ScheduledTaskDto dto) {
        ScheduledTask task = new ScheduledTask();
        task.setId(dto.id);
        //TODO task.setTask(task);
        task.setHakuOid(dto.hakuOid);
        task.setRuntimeForSingle(dto.runtimeForSingle);
        return task;
    }

    @Override
    public ScheduledTaskDto convertToDto(ScheduledTask model) {
        return new ScheduledTaskDto(model.getId(), model.getTask().getName(), model.getHakuOid(), model.getRuntimeForSingle());
    }

}
