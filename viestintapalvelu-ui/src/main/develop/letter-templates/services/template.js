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

'use strict';

angular.module('letter-templates')
    .factory('TemplateService', ['$resource', '$http', '$q', 'Global', function ($resource, $http, $q, Global) {

        var selectedApplicationTarget,
            deferred = $q.defer(),
            template = {},
            baseTemplate;

        $http.get(window.url("viestintapalvelu.options.hakus")).success(function (data) {
            deferred.resolve(data);
        });

        return {
            getTemplatesByOid: function(oidList) {
                return $http.get(window.url("viestintapalvelu.template"), {params: {organizationid: oidList}})
            },
            getDraftsByTags: function(tags) {
                return $http.get(window.url("viestintapalvelu.template.draft"), {params: {tags: tags}})
            },
            getDraftsByOid: function(applicationPeriod, oidList) {
                return $http.get(window.url("viestintapalvelu.template.draftPeriod", applicationPeriod), {params: {organizationid: oidList}})
            },
            getApplicationTargets: function() {
                return deferred.promise;
            },
            getBaseTemplates: function() {
                return $http.get(window.url("viestintapalvelu.template.list"))
            },
            getApplicationTarget: function() {
                return selectedApplicationTarget;
            },
            setApplicationTarget: function(value) {
                selectedApplicationTarget = value;
            },
            setBase: function(base) {
                baseTemplate = base;
            },
            getBase: function() {
                return baseTemplate;
            },
            setLanguage: function(language) {
                template.language = language;
            },
            setTarget: function(target) {
                template.name = target.name;
                template.oid = target.value;
            },
            setType: function(type) {
                template.type = type;
            },
            getTemplate: function() {
                return template;
            },
            saveTemplate: function(template) {
                return $http.post(window.url("viestintapalvelu.template.insert"), template);
            },
            getTemplateByIdAndState: function(id, state) {
                return $http.get(window.url("viestintapalvelu.template.byIdState", id, state));
            },
            getTemplateById: function(id) {
                return $http.get(window.url("viestintapalvelu.template.content", id))
            },
            getTemplatesByApplicationPeriod: function(applicationOid) {
                return $http.get(window.url("viestintapalvelu.template.byPeriod", applicationOid));
            },
            getTemplateByNameStateApplicationPeriodAndLanguage: function(templateName, language, applicationPeriod, state) {
                return $http.get(window.url("viestintapalvelu.template.byName", state), {params: {'templateName':templateName,'languageCode':language,'applicationPeriod':applicationPeriod}})
            },
            updateTemplate: function() {
                return $resource(window.url("viestintapalvelu.template.update"), {}, {
                    put: {
                        method: "PUT"
                    }
                });
            },
            getTemplateContent: function() {
              return $http.get(window.url("viestintapalvelu.template.templateContent", template.type, template.language, template.oid));
            },
            getDefaultTemplates: function() {
                return $http.get(window.url("viestintapalvelu.template.defaults"));
            },
            publishTemplate: function() {
                return true;
            },
            getStructureById: function(structureId) {
                return $http.get(window.url("viestintapalvelu.structure", structureId));
            },
            getDraft: function(templateName, language, applicationPeriod, fetchTarget, organizationOid) {
                return $http.get(window.url("viestintapalvelu.template.getDraft"), {params: {'templateName':templateName,'languageCode':language,'applicationPeriod':applicationPeriod,'fetchTarget':fetchTarget,'oid':organizationOid}})
            },
            getFetchTargetsByOid: function(oid) {
                return $http.get(window.url("viestintapalvelu.options.hakukohde", oid));
            },
            updateDraft: function() {
                return $resource(window.url("viestintapalvelu.template.updateDraft"), {}, {
                    put: {
                        method: "POST"
                    }
                });
            },
            insertDraft: function() {
                return $resource(window.url("viestintapalvelu.template.storeDraft"), {}, {
                    post: {
                        method: "POST"
                    }
                });
            },
            getNameFromHaku: function (haku, prefix) {
                if(!angular.isDefined(prefix)) {
                    prefix = 'kieli_';
                }
                var lang = Global.getUserLanguage();
                var order;
                if(lang === 'fi') {
                    order = ['fi', 'sv', 'en'];
                } else if(lang === 'sv') {
                    order = ['sv', 'fi', 'en'];
                } else {
                    order = ['en', 'fi', 'sv'];
                }
                if(haku.nimi[prefix + order[0]]) { //check unknown, null or ""
                    return haku.nimi[prefix + order[0]];
                }
                if(haku.nimi[prefix + order[1]]) {
                    return haku.nimi[prefix + order[1]];
                }
                if(haku.nimi[prefix + order[2]]) {
                    return haku.nimi[prefix + order[2]];
                }
            }
        }
    }]);