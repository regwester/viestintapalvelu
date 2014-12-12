/**
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import fi.vm.sade.viestintapalvelu.model.ContentStructure;
import fi.vm.sade.viestintapalvelu.model.Template;
import fi.vm.sade.viestintapalvelu.model.types.ContentStructureType;
import fi.vm.sade.viestintapalvelu.template.TemplatesByApplicationPeriod;
import fi.vm.sade.viestintapalvelu.template.TemplateInfo;
import fi.vm.sade.viestintapalvelu.template.TemplatesByApplicationPeriodConverter;

/**
 * @author risal1
 *
 */
@Transactional
@Component
public class TemplatesByApplicationPeriodConverterImpl implements TemplatesByApplicationPeriodConverter {

    @Override
    public TemplatesByApplicationPeriod convert(String applicationPeriod, List<Template> publisheds, List<Template> drafts, List<Template> closeds) {
        List<TemplateInfo> publishedInfos = ImmutableList.copyOf(convertTemplatesToInfo(filterLatests(publisheds)));
        List<TemplateInfo> draftInfos = ImmutableList.copyOf(convertTemplatesToInfo(filterLatests(drafts)));
        List<TemplateInfo> closedInfos = ImmutableList.copyOf(convertTemplatesToInfo(filterCloseds(filterLatests(closeds), publishedInfos, draftInfos)));
        return new TemplatesByApplicationPeriod(applicationPeriod, publishedInfos, draftInfos, closedInfos);
    }

    @Override
    public TemplateInfo convert(Template template) {
        return new TemplateInfo(template.getId(), template.getName(), template.getLanguage(), template.getState(), template.getTimestamp(), parseStructureTypes(template));
    }
    
    @Override
    public List<TemplateInfo> convert(List<Template> templates) {
        return ImmutableList.copyOf(convertTemplatesToInfo(templates));
    }
    
    private Set<ContentStructureType> parseStructureTypes(Template template) {
        Set<ContentStructureType> types = new HashSet<>();
        for (ContentStructure cs : template.getStructure().getContentStructures()) {
            types.add(cs.getType());
        }
        return types;
    }

    private List<Template> filterCloseds(List<Template> closeds, final List<TemplateInfo> publisheds, final List<TemplateInfo> drafts) {
        @SuppressWarnings("unchecked")
        final List<TemplateInfo> pubDrafts = ListUtils.union(publisheds, drafts);
        return new ArrayList<Template>(Collections2.filter(closeds, new Predicate<Template>() {

            @Override
            public boolean apply(final Template template) {
                return !Iterables.tryFind(pubDrafts, new Predicate<TemplateInfo>() {

                    @Override
                    public boolean apply(TemplateInfo input) {
                        return input.language.equals(template.getLanguage()) && input.name.equals(template.getName());
                    }
                    
                }).isPresent();
            }
            
        }));
    }
    
    private List<TemplateInfo> convertTemplatesToInfo(List<Template> templates) {
        return Lists.transform(templates, new Function<Template, TemplateInfo>() {
            
            @Override
            public TemplateInfo apply(Template input) {
                return convert(input);
            }
        });
    }

    private List<Template> filterLatests(final List<Template> templates) {
        return new ArrayList<Template>(Collections2.filter(templates, new Predicate<Template>() {

            @Override
            public boolean apply(final Template template) {
                return !Iterables.tryFind(templates, new Predicate<Template>() {

                    @Override
                    public boolean apply(Template input) {
                        return input.getLanguage().equals(template.getLanguage()) && input.getName().equals(template.getName()) && input.getTimestamp().after(template.getTimestamp());
                    }
                    
                }).isPresent();
            }
            
        }));
    }

}
