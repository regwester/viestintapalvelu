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

package fi.vm.sade.ryhmasahkoposti.util;

import java.util.Arrays;

import org.jsoup.helper.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import fi.vm.sade.ryhmasahkoposti.externalinterface.api.AttachmentResource;
import fi.vm.sade.ryhmasahkoposti.externalinterface.component.AttachmentComponent;

/**
 * User: ratamaa
 * Date: 25.9.2014
 * Time: 15:54
 */
@Component
public class ContextLoadedListener implements ApplicationListener<ContextRefreshedEvent> {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("Spring ContextStarted");
        String beanNames = StringUtil.join(Arrays.asList(event.getApplicationContext().getBeanDefinitionNames()), ", ");
        AttachmentResource resource = event.getApplicationContext().getBean(AttachmentResource.class);
        AttachmentComponent component = event.getApplicationContext().getBean(AttachmentComponent.class);
        component.setAttachmentResource(resource);
        logger.info("BEANS: " + beanNames);
    }
}