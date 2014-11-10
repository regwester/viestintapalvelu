'use strict';

angular.module('letter-templates')
    .factory('templateService', ['$resource', '$http', function($resource, $http) {

    var baseUrl = '/viestintapalvelu-service/api/v1/template/';


    return {
        listByApplicationPeriod: $resource(baseUrl+"listByApplicationPeriod/:applicationPeriod",
            {applicationPeriod: '@applicationPeriod'}),

        status: $resource(baseUrl + 'status')
    };
}]);