
var emailResp = angular.module('viestintapalvelu');

emailResp.controller('EmailResponseController', ['$scope', '$rootScope', 'EmailResultFactory', 'EmailSendStatusFactory', '$location', 
                                                 function($scope, $rootScope, EmailResultFactory, EmailSendStatusFactory, $location) { 	
	
	 $scope.emailsendid = $rootScope.emailsendid;
	 $scope.RaportoitavaViestiDTO = EmailResultFactory.sendResult($scope.emailsendid.id);
	 
		$scope.LahetyksenTilanneDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id).$promise.then(	
				function(value) {				
	            	$scope.LahetyksenTilanneDTO = value; 
				},	            
	            function(error) {
	                alert("Error " + error);
	            },
	            function(update) {
	                alert("Notification " + update);
	            }
	    );
	 
	 		
}]);

