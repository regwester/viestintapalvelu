package fi.vm.sade.viestintapalvelu.exception;

public class ExternalInterfaceException extends AbstractRuntimeException {
    private static final long serialVersionUID = 7441945467124965835L;

    public ExternalInterfaceException(String message) {
        super(message);
    }
    
    public ExternalInterfaceException(Throwable throwable) {
        super(throwable);
    }
    
    public ExternalInterfaceException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
    public ExternalInterfaceException(String message, Throwable throwable, String errorId) {
        super(message, throwable, errorId);
    }
}
