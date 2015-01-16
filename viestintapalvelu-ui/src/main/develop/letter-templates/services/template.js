'use strict';

angular.module('letter-templates')
    .factory('TemplateService', ['$resource', '$http', '$q', function ($resource, $http, $q) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            templateBaseUrl = serviceUrl + 'template/',
            selectedApplicationTarget,
            deferred = $q.defer(),
            template = {};

        $http.get(serviceUrl + 'options/hakus').success(function (data) {
            deferred.resolve(data);
        });

        return {
            getTemplatesByOid: function(oidList) {
                return $http.get(templateBaseUrl, {params: {organizationid: oidList}})
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
                template.base = base;
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
            getTemplatesByApplicationPeriod: function(applicationOid) {
                return $http.get(templateBaseUrl + 'listByApplicationPeriod/' + applicationOid);
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
                return $http.get(templateBaseUrl + 'getDraft', {params: {'templateName':templateName,'languageCode':language,'applicationPeriod':applicationPeriod,'fetchTarget':fetchTarget,'organizationOid':organizationOid}})
            }
        }
    }]);