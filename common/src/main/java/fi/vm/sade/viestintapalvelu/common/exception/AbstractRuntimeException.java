/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

package fi.vm.sade.viestintapalvelu.common.exception;

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
