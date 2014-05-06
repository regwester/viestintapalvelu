
var emailResp = angular.module('viestintapalvelu');

emailResp.controller('EmailResponseController', ['$scope', '$rootScope', 'EmailResultFactory', 'EmailSendStatusFactory', '$location', 
                                                 function($scope, $rootScope, EmailResultFactory, EmailSendStatusFactory, $location) { 	
	
	$scope.tinymceOptions = {
	        readonly : 1,
	        height: 400,
	        width: 600
	};
	 
	$scope.emailsendid = $rootScope.emailsendid;
	
	$scope.ReportedMessageDTO = EmailResultFactory.sendResult($scope.emailsendid.id).$promise.then(	
			function(value) {				
            	$scope.ReportedMessageDTO = value; 

            	$scope.showTo  = $scope.ReportedMessageDTO.emailRecipients.length <= 30;
            	$scope.showCnt = $scope.ReportedMessageDTO.emailRecipients.length >  30;      
            	
            	$scope.recipCnt = $scope.ReportedMessageDTO.emailRecipients.length + " vastaanottajaa";
			},	            
            function(error) {
                alert("Error (sendResult)" + error);
            },
            function(update) {
                alert("Notification " + update);
            }
    );
	
	$scope.SendingStatusDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id).$promise.then(	
			function(value) {				
            	$scope.SendingStatusDTO = value; 
			},	            
            function(error) {
                alert("Error (sendEmailStatus)" + error);
            },
            function(update) {
                alert("Notification " + update);
            }
    );
	 
	 		
}]);

