package fi.vm.sade.ryhmasahkoposti.exception;

public class PersistenceException extends AbstractRuntimeException {

    public PersistenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
