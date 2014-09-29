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

package fi.vm.sade.ryhmasahkoposti.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;

import org.dom4j.DocumentException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import fi.vm.sade.authentication.model.Henkilo;
import fi.vm.sade.authentication.model.OrganisaatioHenkilo;
import fi.vm.sade.organisaatio.resource.dto.OrganisaatioRDTO;
import fi.vm.sade.ryhmasahkoposti.api.dto.EmailAttachment;
import fi.vm.sade.ryhmasahkoposti.api.dto.TemplateDTO;
import fi.vm.sade.ryhmasahkoposti.externalinterface.api.*;
import fi.vm.sade.ryhmasahkoposti.testdata.RaportointipalveluTestData;
import mockit.Mock;
import mockit.MockClass;
import mockit.Mockit;

/**
 * User: ratamaa
 * Date: 25.9.2014
 * Time: 12:38
 */
@Configuration
@ImportResource(value = {"classpath:test-bundle-context.xml"})
public class IntegrationTestConfig {

    public static class MailerStatus {
        private List<Message> messages = new ArrayList<Message>();

        public MailerStatus() {
        }

        public void addSentMessage(Message message) {
            this.messages.add(message);
        }

        public List<Message> getMessages() {
            return messages;
        }
    }

    private static MailerStatus mailerStatus = new MailerStatus();

    @Bean
    MailerStatus mailerStatus() {
        // TODO: Find a way to do this or similar thing (overriding static method) with newer
        // JMockito wihtout it's runner or PwoerMockito without it's runner (seems not possible)
        // (using SpringJUnit4ClassRunner runner)
        Mockit.setUpMock(TrasnportMock.class);
        return mailerStatus;
    }

    @MockClass(realClass = Transport.class)
    public static class TrasnportMock{
        @Mock
        public static void send(Message msg) throws MessagingException {
            mailerStatus.addSentMessage(msg);
        }

        @Mock
        public static void send(Message msg, Address[] addresses)
                throws MessagingException {
            mailerStatus.addSentMessage(msg);
        }
    }

    @Bean
    OmattiedotResource omatTiedotStub() {
        return new OmattiedotResource() {
            @Override
            public Henkilo currentHenkiloTiedot() {
                return RaportointipalveluTestData.getHenkilo();
            }

            @Override
            public List<OrganisaatioHenkilo> currentHenkiloOrganisaatioHenkiloTiedot() {
                throw new IllegalStateException("Please mock me when needed!");
            }
        };
    }

    @Bean
    OrganisaatioResource organisaatioResourceStub() {
        return new OrganisaatioResource() {
            @Override
            public OrganisaatioRDTO getOrganisaatioByOID(String oid) {
                throw new IllegalStateException("Please mock me when needed!");
            }
        };
    }

    @Bean
    AttachmentResource attachmentResourceStub() {
        return new AttachmentResource() {
            @Override
            public EmailAttachment downloadByUri(String uri) {
                throw new IllegalStateException("Please mock me when needed!");
            }

            @Override
            public void deleteByUris(UrisContainerDto urisContainerDto) {
                throw new IllegalStateException("Please mock me when needed!");
            }
        };
    }

    @Bean
    HenkiloResource henkiloResource() {
        return new HenkiloResource() {
            @Override
            public Henkilo findByOid(String oid) {
                throw new IllegalStateException("Please mock me when needed!");
            }
        };
    }

    @Bean
    TemplateResource templateResourceStub() {
        return new TemplateResource() {
            @Override
            public TemplateDTO getTemplateContent(String templateName, String languageCode, String type, String applicationPeriod) throws IOException, DocumentException {
                throw new IllegalStateException("Please mock me when needed!");
            }

            @Override
            public TemplateDTO getTemplateByID(String templateId) {
                throw new IllegalStateException("Please mock me when needed!");
            }
        };
    }

    @Bean
    ThreadPoolTaskExecutor emailExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
