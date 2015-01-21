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
    .factory('TemplateService', ['$resource', '$http', '$q', function ($resource, $http, $q) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            templateBaseUrl = serviceUrl + 'template/',
            selectedApplicationTarget,
            deferred = $q.defer(),
            template = {},
            baseTemplate;

        $http.get(serviceUrl + 'options/hakus').success(function (data) {
            deferred.resolve(data);
        });

        return {
            getTemplatesByOid: function(oidList) {
                return $http.get(templateBaseUrl, {params: {organizationid: oidList}})
            },
            getDraftsByTags: function(tags) {
                return $http.get(templateBaseUrl + "draft", {params: {tags: tags}})
            },
            getDraftsByOid: function(applicationPeriod, oidList) {
                return $http.get(templateBaseUrl + "draft/applicationPeriod/" + applicationPeriod, {params: {organizationid: oidList}})
            },
            getApplicationTargets: function() {
                return deferred.promise;
            },
            getBaseTemplates: function() {
                return $http.get(templateBaseUrl + 'list')
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
                return $http.post(templateBaseUrl + 'insert/', template);
            },
            getTemplateByIdAndState: function(id, state) {
                return $http.get(templateBaseUrl + id + '/getTemplateContent/' + state);
            },
            getTemplateById: function(id) {
                return $http.get(templateBaseUrl + id + '/letter/getTemplateContent/')
            },
            getTemplatesByApplicationPeriod: function(applicationOid) {
                return $http.get(templateBaseUrl + 'listByApplicationPeriod/' + applicationOid);
            },
            getTemplateByNameStateApplicationPeriodAndLanguage: function(templateName, language, applicationPeriod, state) {
                return $http.get(templateBaseUrl + 'getByName/' + state, {params: {'templateName':templateName,'languageCode':language,'applicationPeriod':applicationPeriod}})
            },
            updateTemplate: function() {
                return $resource(templateBaseUrl + 'update', {}, {
                    put: {
                        method: "PUT"
                    }
                });
            },
            getTemplateContent: function() {
              return $http.get(templateBaseUrl + template.type + '/' + template.language + '/letter/' + template.oid + '/getTemplateContent');
            },
            getDefaultTemplates: function() {
                return $http.get(templateBaseUrl + 'defaults');
            },
            publishTemplate: function() {
                return true;
            },
            getStructureById: function(structureId) {
                return $http.get(serviceUrl + 'structure/' + structureId);
            },
            getDraft: function(templateName, language, applicationPeriod, fetchTarget, organizationOid) {
                return $http.get(templateBaseUrl + 'getDraft', {params: {'templateName':templateName,'languageCode':language,'applicationPeriod':applicationPeriod,'fetchTarget':fetchTarget,'oid':organizationOid}})
            },
            getFetchTargetsByOid: function(oid) {
                return $http.get(serviceUrl + '/options/hakukohde/' + oid);
            },
            updateDraft: function() {
                return $resource(templateBaseUrl + 'updateDraft', {}, {
                    put: {
                        method: "PUT"
                    }
                });
            },
            insertDraft: function() {
                return $resource(templateBaseUrl + 'storeDraft', {}, {
                    post: {
                        method: "POST"
                    }
                });
            }
        }
    }]);