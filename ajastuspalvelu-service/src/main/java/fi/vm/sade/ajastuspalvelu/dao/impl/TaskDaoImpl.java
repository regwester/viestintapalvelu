package fi.vm.sade.ajastuspalvelu.dao.impl;



import org.springframework.stereotype.Repository;

import fi.vm.sade.ajastuspalvelu.dao.TaskDao;
import fi.vm.sade.ajastuspalvelu.model.Task;
import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;

@Repository
public class TaskDaoImpl extends AbstractJpaDAOImpl<Task, Long> implements TaskDao {
}
