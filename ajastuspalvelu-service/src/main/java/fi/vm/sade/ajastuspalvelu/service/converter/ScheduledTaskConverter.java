package fi.vm.sade.ajastuspalvelu.service.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.ajastuspalvelu.dao.TaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskListDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskModifyDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskSaveDto;
import fi.vm.sade.viestintapalvelu.common.util.OptionalHelper;

import static com.google.common.base.Optional.fromNullable;

@Component("scheduledTaskConverter")
public class ScheduledTaskConverter {

    @Autowired
    private TaskDao taskDao;

    public ScheduledTaskListDto convert(ScheduledTask model) {
        return new ScheduledTaskListDto(model.getId(), model.getTask().getName(),
                model.getHakuOid(), model.getRuntimeForSingle(), model.getRemoved());
    }

    public ScheduledTask convert(ScheduledTaskSaveDto from, ScheduledTask to) {
        to.setTask(fromNullable(taskDao.read(from.getTaskId()))
            .or(OptionalHelper.<Task>notFound("Task not found by id="+from.getTaskId())));
        to.setHakuOid(from.getHakuOid());
        to.setRuntimeForSingle(from.getRuntimeForSingle());
        return to;
    }

    public ScheduledTaskModifyDto convert(ScheduledTask from, ScheduledTaskModifyDto to) {
        to.setId(from.getId());
        to.setTaskId(from.getTask() != null ? from.getTask().getId() : null);
        to.setHakuOid(from.getHakuOid());
        to.setRuntimeForSingle(from.getRuntimeForSingle());
        return to;
    }
}
