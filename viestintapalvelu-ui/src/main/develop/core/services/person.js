'use strict';

angular.module('core.services').factory('PersonService', ['$http', function ($http) {
    
    var serviceUrl = '/viestintapalvelu/api/v1/';
    
    return {
	getPerson : function(oid) {
	    return $http({method:"GET", withCredentials:true, url:serviceUrl + 'person/' + oid});
	}
    } 
}]);