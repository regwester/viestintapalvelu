
var emailCancel = angular.module('viestintapalvelu');

emailCancel.controller('EmailCancelController', ['$scope', '$rootScope', '$location', function($scope, $rootScope, $location) { 	
//	alert("EmailCancelController");
	
	 $scope.callingProcess = $rootScope.callingProcess;
	 
}]);

