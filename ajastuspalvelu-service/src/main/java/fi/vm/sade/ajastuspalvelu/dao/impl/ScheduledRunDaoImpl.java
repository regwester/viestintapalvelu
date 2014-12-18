package fi.vm.sade.ajastuspalvelu.dao.impl;

import org.springframework.stereotype.Repository;

import fi.vm.sade.ajastuspalvelu.dao.ScheduledRunDao;
import fi.vm.sade.ajastuspalvelu.model.ScheduledRun;
import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;

@Repository
public class ScheduledRunDaoImpl extends AbstractJpaDAOImpl<ScheduledRun, Long>
        implements ScheduledRunDao {

}
