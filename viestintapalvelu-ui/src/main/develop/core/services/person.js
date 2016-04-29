'use strict';

angular.module('core.services').factory('PersonService', ['$http', function ($http) {
	return {
	getPerson : function(oid) {
	    return $http({method:"GET", withCredentials:true, url:window.url("viestintapalvelu.person", oid)});
	},
	getRights : function() {
	    return $http({method:"GET", withCredentials:true, url:window.url("viestintapalvelu.personUserRights")});
	}
    } 
}]);