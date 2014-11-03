package fi.vm.sade.ajastuspalvelu.service.converter;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import fi.vm.sade.ajastuspalvelu.dao.TaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskListDto;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskModifyDto;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledTaskConverterTest {

    private static final DateTime RUNTIME = new DateTime(), RUNTIME_TASK = new DateTime(500000l);

    private static final String HAKU_OID = "1.9.2.3", HAKU_OID_TASK = "1.2.3.4";

    private static final Long TASK_ID = 123l;

    @Mock
    private TaskDao dao;
    
    @InjectMocks
    private ScheduledTaskConverter converter;

    @Test
    public void convertsDtoToModelUsingExistingModel() {
        long id = 5l;
        ScheduledTask task = givenScheduledTask(id);
        when(dao.read(TASK_ID)).thenReturn(new Task());
        assertModel(converter.convert(new ScheduledTaskModifyDto(id, TASK_ID, HAKU_OID, RUNTIME), task), id);
    }

    @Test
    public void convertsModelToDto() {
        long id = 3l;
        ScheduledTaskListDto dto = converter.convert(givenScheduledTask(id));
        assertEquals(new ScheduledTaskListDto(id, null, HAKU_OID_TASK, RUNTIME_TASK), dto);
    }

    private void assertModel(ScheduledTask task, Long expectedId) {
        assertEquals(expectedId, task.getId());
        assertEquals(HAKU_OID, task.getHakuOid());
        assertEquals(RUNTIME, task.getRuntimeForSingle());
    }
    
    private ScheduledTask givenScheduledTask(long id) {
        ScheduledTask task = new ScheduledTask();
        task.setId(id);
        task.setTask(new Task());
        task.setRuntimeForSingle(RUNTIME_TASK);
        task.setHakuOid(HAKU_OID_TASK);
        return task;
    }
}
