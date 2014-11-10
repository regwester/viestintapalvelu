'use strict';

angular.module('letter-templates')
    .factory('templateService', ['$resource', '$http', function ($resource, $http) {

        var baseUrl = '/viestintapalvelu-service/api/v1/template/';

        return {

            getHakus: function() {
                return $http.get(baseUrl + 'haku');
            },

            getByApplicationPeriod: function (applicationPeriod) {
                return $http.get(baseUrl + 'listByApplicationPeriod/' + applicationPeriod);
            }
        };
    }]);