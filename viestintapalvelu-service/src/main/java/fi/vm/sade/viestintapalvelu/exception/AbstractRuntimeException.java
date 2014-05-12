package fi.vm.sade.viestintapalvelu.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

public abstract class AbstractRuntimeException extends RuntimeException {
    private static final long serialVersionUID = -3938248680738145281L;
    private String errorID;
    private String orginalException;
    private String orginalExceptionMessage;
    private String nestedStackTrace;

    public AbstractRuntimeException(String message) {
        this(message, null, null);
    }
    
    public AbstractRuntimeException(Throwable throwable) {
        this(throwable.getMessage(), throwable, null);
    }
    
    public AbstractRuntimeException(String message, Throwable throwable) {
        this(message, throwable, null);
    }
    
    public AbstractRuntimeException(String message, Throwable throwable, String errorId) {
        super(message);
        
        if (throwable != null) {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);

            Throwable cause = throwable;
            while (cause != null) {
                cause.printStackTrace(printWriter);
                
                if (cause instanceof SQLException) {
                    cause = ((SQLException)cause).getNextException();
                }
                else {
                    cause = cause.getCause();
                }
            }
            
            this.nestedStackTrace = stringWriter.getBuffer().toString();
            this.orginalExceptionMessage = throwable.getMessage();
            this.orginalException = throwable.getClass().getName();
        }

        if (errorId != null) {
            this.errorID = errorId;
        }
    }

    public String getErrorID() {
        return errorID;
    }

    public void setErrorID(String errorID) {
        this.errorID = errorID;
    }

    public String getOrginalException() {
        return orginalException;
    }
    
    public void setOrginalException(String orginalException) {
        this.orginalException = orginalException;
    }
    
    public String getOrginalExceptionMessage() {
        return orginalExceptionMessage;
    }
    
    public void setOrginalExceptionMessage(String orginalExceptionMessage) {
        this.orginalExceptionMessage = orginalExceptionMessage;
    }

    public String getNestedStackTrace() {
        return nestedStackTrace;
    }

    public void setNestedStackTrace(String nestedStackTrace) {
        this.nestedStackTrace = nestedStackTrace;
    }
}
