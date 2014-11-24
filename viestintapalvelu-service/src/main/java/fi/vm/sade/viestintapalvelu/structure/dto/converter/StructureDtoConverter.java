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

package fi.vm.sade.viestintapalvelu.structure.dto.converter;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fi.vm.sade.viestintapalvelu.dao.StyleDAO;
import fi.vm.sade.viestintapalvelu.model.*;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentReplacementSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureContentSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.ContentStructureSaveDto;
import fi.vm.sade.viestintapalvelu.structure.dto.StructureSaveDto;
import fi.vm.sade.viestintapalvelu.common.util.OptionalHelper;
import fi.vm.sade.viestintapalvelu.util.dtoconverter.AbstractDtoConverter;
import fi.vm.sade.viestintapalvelu.common.util.impl.BeanValidatorImpl;

/**
 * User: ratamaa
 * Date: 10.11.2014
 * Time: 14:28
 */
@Component
public class StructureDtoConverter extends AbstractDtoConverter {

    @Autowired
    private StyleDAO styleDAO;

    public Structure convert(StructureSaveDto from, Structure to) {
        convertValue(from, to);
        for (ContentStructure cs : to.getContentStructures()) {
            cs.setStructure(to);
        }

        int orderNumber = 1;
        Set<String> usedKeys = new HashSet<String>();
        for (ContentReplacementSaveDto dto : from.getReplacements()) {
            if (!usedKeys.add(dto.getKey())) {
                throw BeanValidatorImpl.badRequest("Replacements contained key: " + dto.getKey() + " twice.");
            }
            ContentReplacement replacement = convert(dto, new ContentReplacement());
            replacement.setOrderNumber(orderNumber++);
            replacement.setStructure(to);
            to.getReplacements().add(replacement);
        }
        return to;
    }

    public ContentStructure convert(ContentStructureSaveDto from, ContentStructure to) {
        convertValue(from, to);

        if (from.getStyleName() != null && from.getStyle() == null) {
            to.setStyle(styleDAO.findLatestByName(from.getStyleName())
                    .or(OptionalHelper.<Style>notFound("Style not found by name="+from.getStyleName())));
        } else if (to.getStyle() != null && to.getStyle().getId() == null) {
            styleDAO.insert(to.getStyle());
        }
        int orderNumber = 1;
        for (ContentStructureContentSaveDto contentDto : from.getContents()) {
            ContentStructureContent csc = convert(contentDto, new ContentStructureContent());
            csc.setContentStructure(to);
            csc.setOrderNumber(orderNumber++);
            to.getContents().add(csc);
        }
        return to;
    }
}
