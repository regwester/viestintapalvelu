
var emailCancel = angular.module('viestintapalvelu');

emailCancel.controller('EmailSendStatusController', ['$scope', '$rootScope', '$location', function($scope, $rootScope, $location) { 	
//	alert("EmailSendStatusController");
	
	 $scope.sendStarted = $rootScope.sendStarted;
	 
}]);

