package fi.vm.sade.ajastuspalvelu.service.converter;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import fi.vm.sade.ajastuspalvelu.dao.ScheduledTaskDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledTask;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.ajastuspalvelu.service.dto.ScheduledTaskDto;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledTaskConverterTest {

    private static final DateTime RUNTIME = new DateTime(), RUNTIME_TASK = new DateTime(500000l);

    private static final String HAKU_OID = "1.9.2.3", HAKU_OID_TASK = "1.2.3.4";

    private static final String TASK_NAME = "taskName";

    @Mock
    private ScheduledTaskDao dao;
    
    @InjectMocks
    private ScheduledTaskConverter converter;
    
    @Test
    public void convertsDtoToModel() {
        assertModel(converter.convertToModel(new ScheduledTaskDto(null, TASK_NAME, HAKU_OID, RUNTIME)), null);
    }

    @Test
    public void convertsDtoToModelUsingExistingModel() {
        long id = 5l;
        ScheduledTask task = givenScheduledTask(id);
        when(dao.read(id)).thenReturn(task);
        assertModel(converter.convertToModel(new ScheduledTaskDto(id, TASK_NAME, HAKU_OID, RUNTIME)), id);
    }

    
    @Test
    public void convertsModelToDto() {
        long id = 3l;
        ScheduledTaskDto dto = converter.convertToDto(givenScheduledTask(id));
        assertEquals(new ScheduledTaskDto(id, null, HAKU_OID_TASK, RUNTIME_TASK), dto);
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
