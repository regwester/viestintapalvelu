'use strict';

angular.module('letter-templates')
    .factory('TemplateService', ['$resource', '$http','$q', function ($resource, $http, $q) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            templateBaseUrl = serviceUrl + 'template/',
            selectedApplicationTarget,
            applicationTargets,
            deferred = $q.defer();

        $http.get(serviceUrl + 'options/hakus').success(function(data) {
            deferred.resolve(data);
        });

        return {
            getByApplicationPeriod: function (applicationPeriod) {
                return $http.get(templateBaseUrl + 'listByApplicationPeriod/' + applicationPeriod);
            },
            getApplicationTargets: function() {
                return deferred.promise;
            },
            getApplicationTarget: function() {
                return selectedApplicationTarget;
            },
            setApplicationTarget: function(value) {
                selectedApplicationTarget = value;
            }

        };
    }]);