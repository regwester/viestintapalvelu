'use strict';

angular.module('letter-templates')
    .factory('TemplateService', ['$resource', '$http', function ($resource, $http) {

        var serviceUrl = '/viestintapalvelu/api/v1/';
        var templateBaseUrl = serviceUrl + 'template/';

        return {

            getHakus: function() {
                return $http.get(serviceUrl + 'options/hakus');
            },

            getByApplicationPeriod: function (applicationPeriod) {
                return $http.get(templateBaseUrl + 'listByApplicationPeriod/' + applicationPeriod);
            }
        };
    }]);