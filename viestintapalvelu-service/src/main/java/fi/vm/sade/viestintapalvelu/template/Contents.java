/*
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
 */
package fi.vm.sade.viestintapalvelu.template;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;

/**
 * User: ratamaa Date: 28.10.2014 Time: 13:08
 */
public class Contents implements Predicate<String> {
    public static final String ASIOINTITILI_HEADER = "asiointitili_header";
    public static final String ASIOINTITILI_CONTENT = "asiointitili_content";
    public static final String ASIOINTITILI_SMS_CONTENT = "sms_content";
    public static final String EMAIL_SUBJECT = "email_subject";
    public static final String EMAIL_BODY = "email_body";
    public static final String ATTACHMENT = "liite";
    public static final String  TEST_PLEASE_IGNORE = "test_please_ignore";
    public static final String ACCESSIBLE_HTML_CONTENT = "accessible_html_content";
    public static final String LETTER_CONTENT_CONTENT_NAME = "letter_content";

    protected static final String[] NON_ATTACHMENTS = new String[] { ASIOINTITILI_HEADER, ASIOINTITILI_CONTENT, ASIOINTITILI_SMS_CONTENT, EMAIL_SUBJECT,
            EMAIL_BODY };

    private Set<String> include = new HashSet<>();
    private Set<String> exclude = new HashSet<>();

    protected Contents() {
    }

    protected Contents exclude(String... names) {
        this.exclude.addAll(Arrays.asList(names));
        return this;
    }

    protected Contents include(String... names) {
        this.include.addAll(Arrays.asList(names));
        return this;
    }

    public static Contents pdfLetterContents() {
        return new Contents().include(LETTER_CONTENT_CONTENT_NAME);
    }

    public static Contents attachmentsFor(String templateNameAsMainContent) {
        return new Contents().exclude(NON_ATTACHMENTS).exclude(templateNameAsMainContent);
    }

    public static Contents accessibleHtmlContents() {
        return new Contents().include(ACCESSIBLE_HTML_CONTENT);
    }

    public Collection<TemplateContent> filter(Collection<TemplateContent> contents) {
        return Collections2.filter(contents, forTemplateContent());
    }

    public Predicate<? super TemplateContent> forTemplateContent() {
        return new Predicate<TemplateContent>() {
            public boolean apply(TemplateContent content) {
                return Contents.this.apply(content.getName());
            }
        };
    }

    @Override
    public boolean apply(String contentName) {
        if (contentName == null) {
            return false;
        }
        String name = contentName.toLowerCase().trim();
        if (exclude.contains(name)) {
            return include.contains(name);
        }
        return include.isEmpty() || include.contains(name);
    }
}
