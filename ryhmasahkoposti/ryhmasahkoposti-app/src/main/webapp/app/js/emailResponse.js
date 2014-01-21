
var emailResp = angular.module('viestintapalvelu');

emailResp.controller('EmailResponseController', ['$scope', '$rootScope', 'EmailResultFactory', '$location', 
                                                 function($scope, $rootScope, EmailResultFactory, $location) { 	
//	alert("EmailResponseController");
	
	 $scope.emailsendid = $rootScope.emailsendid;
	 $scope.RaportoitavaViesti = EmailResultFactory.sendResult($scope.emailsendid.id);
	 
	 		
}]);

