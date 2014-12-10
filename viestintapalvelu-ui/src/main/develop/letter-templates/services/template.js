'use strict';

angular.module('letter-templates')
    .factory('TemplateService', ['$resource', '$http','$q', function ($resource, $http, $q) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            templateBaseUrl = serviceUrl + 'template/',
            selectedApplicationTarget,
            deferred = $q.defer();

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
            getApplicationTargets: function() {
                return deferred.promise;
            },
            getApplicationTarget: function() {
                return selectedApplicationTarget;
            },
            setApplicationTarget: function(value) {
                selectedApplicationTarget = value;
            },
            saveTemplate: function(template) {
                return $http.post(templateBaseUrl + 'insert/', template);
            },
            getTemplateByIdAndState: function(id, state) {
        	return $http.get(templateBaseUrl + id + '/getTemplateContent/' + state);
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
            }
        }
    }]);