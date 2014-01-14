
var emailResp = angular.module('viestintapalvelu');

emailResp.controller('EmailResponseController', ['$scope', '$rootScope', '$location', function($scope, $rootScope, $location) { 	
//	alert("EmailResponseController");
	
	 $scope.emailresponse = $rootScope.emailresponse;
	
//	$scope.test = function () {
//		alert("TEST pressed");
//		$location.path("/response");
//	}
		
}]);

