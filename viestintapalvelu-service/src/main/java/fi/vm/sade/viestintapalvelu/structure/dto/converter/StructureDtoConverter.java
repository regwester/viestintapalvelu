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
package fi.vm.sade.viestintapalvelu.structure.dto.converter;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;

import fi.vm.sade.viestintapalvelu.common.util.OptionalHelper;
import fi.vm.sade.viestintapalvelu.common.util.impl.BeanValidatorImpl;
import fi.vm.sade.viestintapalvelu.dao.StyleDAO;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentReplacementSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureContentSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;
import fi.vm.sade.viestintapalvelu.util.dtoconverter.AbstractDtoConverter;

import static com.google.common.base.Optional.fromNullable;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:28
 */
@Component
public class StructureDtoConverter extends AbstractDtoConverter {
    public class ContentStore<T extends NamedContent> {
        private Map<String,T> contentsByName = new HashMap<>();

        public Optional<T> get(String name) {
            return fromNullable(contentsByName.get(name));
        }

        public Optional<T> add(T content) {
            return fromNullable(this.contentsByName.put(content.getName(), content));
        }

        public Collection<T> getContents() {
            return this.contentsByName.values();
        }
    }

    @Autowired
    private StyleDAO styleDAO;

    public StructureSaveDto convert(Structure from, StructureSaveDto to) {
        convertValue(from, to, new ContentStore<Content>());
        return to;
    }

    public ContentStructureSaveDto convert(ContentStructure from, ContentStructureSaveDto to,ContentStore<Content> contentStore) {
        convertValue(from, to);
        for (ContentStructureContent csc : ContentStructureContent.BY_ORDER_NUMBER.sortedCopy(from.getContents())) {
            ContentStructureContentSaveDto dto = convert(csc, new ContentStructureContentSaveDto());
            Optional<Content> alreadyBoundByName =  contentStore.add(csc.getContent());
            if (alreadyBoundByName.isPresent() && alreadyBoundByName.get().getContent().equals(dto.getContent())) {
                dto.setContent(null);
            }
            to.getContents().add(dto);
        }
        return to;
    }

    public Structure convert(StructureSaveDto from, Structure to) {
        ContentStore<Content> contentStore = new ContentStore<>();
        convertValue(from, to, contentStore);
        for (ContentStructure cs : to.getContentStructures()) {
            cs.setStructure(to);
        }

        int orderNumber = 1;
        Set<String> usedKeys = new HashSet<>();
        for (ContentReplacementSaveDto dto : from.getReplacements()) {
            if (!usedKeys.add(dto.getKey())) {
                throw BeanValidatorImpl.badRequest("Replacements contained key: " + dto.getKey() + " twice.");
            }
            ContentReplacement replacement = convert(dto, new ContentReplacement());
            replacement.setOrderNumber(orderNumber++);
            replacement.setStructure(to);
            to.getReplacements().add(replacement);
        }
        validate(contentStore);
        return to;
    }

    protected void validate(ContentStore<Content> store) {
        // Each content should have content specified at some point:
        for (Content content : store.getContents()) {
            if (content.getContent() == null) {
                throw BeanValidatorImpl.badRequest("Content " + content.getName() + ": no content specified in any instance.");
            }
        }
    }

    public ContentStructure convert(ContentStructureSaveDto from, ContentStructure to, ContentStore<Content> contentStore) {
        convertValue(from, to);

        if (from.getStyleName() != null && from.getStyle() == null) {
            to.setStyle(styleDAO.findLatestByName(from.getStyleName())
                    .or(OptionalHelper.<Style>notFound("Style not found by name="+from.getStyleName())));
        } else if (to.getStyle() != null && to.getStyle().getId() == null) {
            styleDAO.insert(to.getStyle());
        }
        int orderNumber = 1;
        for (ContentStructureContentSaveDto contentDto : from.getContents()) {
            ContentStructureContent csc = new ContentStructureContent();
            convert(contentDto, csc);
            Optional<Content> referencedContent = contentStore.get(csc.getContent().getName());
            if (referencedContent.isPresent()) {
                // get content from previous conversions by name...
                csc.setContent(referencedContent.get());
                if (contentDto.getContent() != null
                        && csc.getContent().getContent() == null) {
                    // and update the content of it (if not already specified):
                    csc.getContent().setContent(contentDto.getContent());
                }
            } else {
                // ...or save new
                contentStore.add(csc.getContent());
            }
            csc.setContentStructure(to);
            csc.setOrderNumber(orderNumber++);
            to.getContents().add(csc);
        }
        return to;
    }
}
