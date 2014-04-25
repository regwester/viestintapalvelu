package fi.vm.sade.viestintapalvelu.dao.impl;

import org.springframework.stereotype.Repository;

import fi.vm.sade.generic.dao.AbstractJpaDAOImpl;
import fi.vm.sade.viestintapalvelu.dao.LetterReceiverLetterDAO;
import fi.vm.sade.viestintapalvelu.model.LetterReceiverLetter;

@Repository
public class LetterReceiverLetterDAOImpl extends AbstractJpaDAOImpl<LetterReceiverLetter, Long> implements LetterReceiverLetterDAO {
}
