/**
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 **/
package fi.vm.sade.viestintapalvelu.util.ws;

import java.util.logging.Logger;

import org.apache.cxf.common.logging.LogUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.message.Message;

import fi.vm.sade.viestintapalvelu.util.HetuPrinterUtil;

/**
 * User: ratamaa
 * Date: 15.10.2014
 * Time: 8:32
 */
public class CustomLoggingInInterceptor extends LoggingInInterceptor {
    private static final Logger LOG = LogUtils.getLogger(LoggingOutInterceptor.class);
    private boolean enabled = true;

    @Override
    public void handleMessage(Message message) throws Fault {
        if (enabled) {
            super.handleMessage(message);
        }
    }

    @Override
    public void handleFault(Message message) {
        if (enabled) {
            super.handleFault(message);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    protected Logger getLogger() {
        return LOG;
    }

    protected String transform(String originalLogString) {
        return HetuPrinterUtil.clearHetu(originalLogString);
    }
}
