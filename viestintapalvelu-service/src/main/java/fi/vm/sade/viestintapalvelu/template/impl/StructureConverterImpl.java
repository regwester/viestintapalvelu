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

package fi.vm.sade.viestintapalvelu.template.impl;

import java.util.*;

import org.springframework.stereotype.Component;

import com.google.common.collect.Collections2;

import fi.vm.sade.viestintapalvelu.model.ContentStructure;
import fi.vm.sade.viestintapalvelu.model.ContentStructureContent;
import fi.vm.sade.viestintapalvelu.model.types.ContentRole;
import fi.vm.sade.viestintapalvelu.model.types.ContentType;
import fi.vm.sade.viestintapalvelu.structure.dto.constraint.ContentStructurePredicate;
import fi.vm.sade.viestintapalvelu.structure.dto.constraint.ContentStructureValidator;
import fi.vm.sade.viestintapalvelu.template.Contents;
import fi.vm.sade.viestintapalvelu.template.StructureConverter;
import fi.vm.sade.viestintapalvelu.template.TemplateContent;

/**
 * User: ratamaa
 * Date: 13.11.2014
 * Time: 14:08
 */
@Component
public class StructureConverterImpl implements StructureConverter {
    public static final String LETTER_CONTENT_CONTENT_NAME = "letter_content";
    private ContentStructureValidator validator = new ContentStructureValidator();

    @Override
    public List<TemplateContent> toContents(ContentStructure structure) {
        return toContents(structure, null);
    }

    @Override
    public List<TemplateContent> toContents(ContentStructure structure, ContentStructurePredicate predicate) {
        List<TemplateContent> results = new ArrayList<TemplateContent>();
        if (!isValid(structure)) {
            throw new IllegalStateException("Invalid contentStructure="+structure.getId());
        }

        switch (structure.getType()) {
        case email:
            add(results, convert(Contents.EMAIL_SUBJECT, predicate, firstContent(structure,
                    where(predicate, ContentRole.header).type(ContentType.html))));
            add(results, convert(Contents.EMAIL_BODY, predicate, firstContent(structure,
                    where(predicate, ContentRole.body).type(ContentType.html))));
            add(results, convert(Contents.ATTACHMENT, predicate, contents(structure,
                    where(predicate, ContentRole.attachment).type(ContentType.html))));
            break;
        case asiointitili:
            add(results, convert(Contents.ASIOINTITILI_HEADER, predicate, firstContent(structure,
                    where(predicate, ContentRole.header).type(ContentType.plain))));
            add(results, convert(Contents.ASIOINTITILI_CONTENT, predicate, firstContent(structure,
                    where(predicate, ContentRole.body).type(ContentType.plain))));
            add(results, convert(Contents.ASIOINTITILI_SMS_CONTENT, predicate, firstContent(structure,
                    where(predicate, ContentRole.sms).type(ContentType.plain))));
            add(results, convert(Contents.ATTACHMENT, predicate, contents(structure,
                    where(predicate, ContentRole.attachment).type(ContentType.html))));
            break;
        case letter:
            add(results, convert(LETTER_CONTENT_CONTENT_NAME, predicate, contents(structure,
                    where(predicate).type(ContentType.html))));
            break;
        default: throw new IllegalStateException("Unimplemented ContentStructureType="+structure.getType());
        }
        if (predicate != null && !predicate.isSatisfied()) {
            throw new IllegalStateException("Not sataified ContentStructurePredicate: " +predicate);
        }
        return results;
    }

    protected void add(List<TemplateContent> to, List<TemplateContent> contents) {
        if (contents != null) {
            for (TemplateContent toAdd : contents) {
                add(to, toAdd);
            }
        }
    }

    protected void add(List<TemplateContent> to, TemplateContent content) {
        if (content != null) {
            to.add(content);
            content.setOrder(to.size());
        }
    }

    protected List<TemplateContent> convert(String name, ContentStructurePredicate predicate, Collection<ContentStructureContent> from) {
        List<TemplateContent> results = new ArrayList<TemplateContent>();
        for (ContentStructureContent csc : from) {
            TemplateContent content = convert(name, predicate, csc);
            if (content != null) {
                results.add(content);
            }
        }
        return results;
    }

    protected TemplateContent convert(String name, ContentStructurePredicate predicate, ContentStructureContent from) {
        if (from == null) {
            return null;
        }
        if (predicate != null && !predicate.consumes(from)) {
            return null;
        }
        TemplateContent content = new TemplateContent();
        content.setId(from.getContent().getId());
        content.setName(name);
        content.setTimestamp(from.getContentStructure().getTimestamp());
        content.setContent(from.getContent().getContent());
        return content;
    }

    protected boolean isValid(ContentStructure structure) {
        return validator.isValid(structure, null);
    }

    protected ContentStructurePredicate where(ContentStructurePredicate predicate, ContentRole ...roles) {
        return new ContentStructurePredicate(roles).and(predicate);
    }

    protected ContentStructureContent firstContent(ContentStructure structure, ContentStructurePredicate predicate) {
        List<ContentStructureContent> contents = contents(structure, predicate);
        if (contents.isEmpty()) {
            return null;
        }
        return contents.iterator().next();
    }

    protected List<ContentStructureContent> contents(ContentStructure structure, ContentStructurePredicate predicate) {
        List<ContentStructureContent> contents = new ArrayList<ContentStructureContent>(
                Collections2.filter(structure.getContents(), predicate));
        Collections.sort(contents, new Comparator<ContentStructureContent>() {
            @Override
            public int compare(ContentStructureContent o1, ContentStructureContent o2) {
                return Integer.valueOf(o1.getOrderNumber()).compareTo(o2.getOrderNumber());
            }
        });
        return contents;
    }
}
