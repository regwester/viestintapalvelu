package fi.vm.sade.ajastuspalvelu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import fi.vm.sade.ajastuspalvelu.dao.TaskDao;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.TaskService;
import fi.vm.sade.ajastuspalvelu.service.converter.TaskConverter;
import fi.vm.sade.ajastuspalvelu.service.dto.TaskListDto;

@Service
public class TaskServiceImpl implements TaskService {
    
    @Autowired
    private TaskDao dao;
    
    @Autowired
    private TaskConverter converter;
    
    @Transactional
    @Override
    public List<TaskListDto> list() {
        List<Task> tasks = dao.findAll();
        return Lists.transform(tasks, new Function<Task, TaskListDto>() {

            @Override
            public TaskListDto apply(Task input) {
                return converter.convert(input);
            }
        });
    }

}
