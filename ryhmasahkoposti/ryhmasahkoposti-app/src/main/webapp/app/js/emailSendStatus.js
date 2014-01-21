
var emailCancel = angular.module('viestintapalvelu');

emailCancel.controller('EmailSendStatusController', ['$scope', '$rootScope', 'EmailSendStatusFactory', '$location', 
                                                     function($scope, $rootScope, EmailSendStatusFactory, $location) { 	
//emailCancel.controller('EmailSendStatusController', ['$scope', '$rootScope', '$location', 
//                                                     function($scope, $rootScope, $location) { 	
//	alert("EmailSendStatusController");

	//	$scope.emailsendid          = GroupEmailFactory.sendGroupEmail($scope.emaildata).$promise.then(
			
	$scope.emailsendid = $rootScope.emailsendid;
	$scope.LahetyksenTilanneDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id).$promise.then(	
            function(value) {
                alert("Success " + value);
                
            	$scope.LahetyksenTilanneDTO = value; 
            	
        		var ok = $scope.LahetyksenTilanneDTO.lahetysOnnistuiLukumaara;
        		var notOk = $scope.LahetyksenTilanneDTO.lahetysEpaonnistuiLukumaara;
        		var all = $scope.LahetyksenTilanneDTO.vastaanottajienLukumaara;
        		
        		if (all == ok + notOk) {
        			$location.path("/response");

        		} else {
        			$location.path("/status");                        
        		}
            	
            },
            function(error) {
                alert("Error " + error);
            },
            function(update) {
                alert("Notification " + update);
            }
    );
	
	
	
	
}]);
