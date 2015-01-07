'use strict';

angular.module('letter-templates')
    .factory('TemplateService', ['$resource', '$http','$q', function ($resource, $http, $q) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            templateBaseUrl = serviceUrl + 'template/',
            selectedApplicationTarget,
            deferred = $q.defer(),
            templateInfo;

        $http.get(serviceUrl + 'options/hakus').success(function(data) {
            deferred.resolve(data);
        });

        return {
            getHakus: function() {
                return $http.get(serviceUrl + 'options/hakus');
            },
            getTemplatesByOid: function(oidList) {
                return $http.get(templateBaseUrl, {params: {organizationid: oidList }})
            },
            getDraftsByOid: function(applicationPeriod, oidList) {
                return $http.get(templateBaseUrl+"draft/applicationPeriod/"+applicationPeriod, {params: {organizationid: oidList }})
            },
            getApplicationTargets: function() {
                return deferred.promise;
            },
            getApplicationTarget: function() {
                return selectedApplicationTarget;
            },
            setApplicationTarget: function(value) {
                selectedApplicationTarget = value;
            },
            setTemplateInfo: function(value) {
                templateInfo = value;
            },
            getTemplateInfo: function() {
                return templateInfo;
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
            getDefaultTemplates: function() {
        	return $http.get(templateBaseUrl + 'defaults');
            },
            publishTemplate : function() {
                return true;
            },
            getStructureById : function(structureId) {
        	return $http.get(serviceUrl + 'structure/' + structureId);
            }
        }
    }]);