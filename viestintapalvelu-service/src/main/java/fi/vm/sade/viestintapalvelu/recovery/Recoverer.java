package fi.vm.sade.viestintapalvelu.recovery;

/**
 * @see fi.vm.sade.viestintapalvelu.recovery.RecovererPriority
 */
public interface Recoverer {
    
    public Runnable getTask();
}
