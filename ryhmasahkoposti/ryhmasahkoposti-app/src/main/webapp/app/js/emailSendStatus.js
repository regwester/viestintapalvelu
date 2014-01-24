
var emailStatus = angular.module('viestintapalvelu');

emailStatus.controller('EmailSendStatusController', ['$scope', '$rootScope', 'EmailSendStatusFactory', '$location', '$timeout', 
                                                     function($scope, $rootScope, EmailSendStatusFactory, $location, $timeout) { 	
//emailCancel.controller('EmailSendStatusController', ['$scope', '$rootScope', '$location', 
//                                                     function($scope, $rootScope, $location) { 	

//	alert("EmailSendStatusController");
	$scope.percentage = 0;
			
	$scope.emailsendid = $rootScope.emailsendid;
	
	$scope.testi='>>>>>>>>>>>';
	
	$scope.LahetyksenTilanneDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id).$promise.then(	
			function(value) {				
//              alert("Success " + value);
            	$scope.LahetyksenTilanneDTO = value; 
            	
        		var ok = $scope.LahetyksenTilanneDTO.lahetysOnnistuiLukumaara;
        		var notOk = $scope.LahetyksenTilanneDTO.lahetysEpaonnistuiLukumaara;
        		var all = $scope.LahetyksenTilanneDTO.vastaanottajienLukumaara;
        		
        		$scope.percentage = parseInt((ok + notOk) / all * 100, 10);    		
//        		$scope.percentage = parseInt((ok + notOk +1) / (all + 1) * 100, 10);        		
//        		
//        		$timeout(function() {
//            		$scope.percentage = parseInt((ok + notOk +2) / (all + 1) * 100, 10);        		        			
//        		}, 2000);

        		
        		if (all == ok + notOk) {
        			$location.path("/response");        			

        		} else {
//        			$timeout(function() {
//        				// Wait 2 sec for next round        		        			
//        				$scope.testi='!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!';
//        				$location.path("/status");
//    				}, 5000);       		
////        			$location.path("/status");                        
        		}
        		
        		$location.path("/response");        		
            	
            },
            
            function(error) {
                alert("Error " + error);
            },
            function(update) {
                alert("Notification " + update);
            }
    );
	
}]);
