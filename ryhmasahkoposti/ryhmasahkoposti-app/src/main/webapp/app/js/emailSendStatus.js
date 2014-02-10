
var emailStatus = angular.module('viestintapalvelu');

emailStatus.controller('EmailSendStatusController', ['$scope', '$rootScope', 'EmailSendStatusFactory', '$location', '$timeout', 
                                                     function($scope, $rootScope, EmailSendStatusFactory, $location, $timeout) { 	

	$scope.emailsendid = $rootScope.emailsendid;

	$scope.getStatus = function () {
//		alert("$scope.getStatus");
		
		$scope.SendingStatusDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id);	
	
		$scope.percentage = function (ok, notOk, all) {
			var o = parseInt(ok);
			var nO =  parseInt(notOk)
			var a = parseInt(all);
			
			if (all == ok + notOk) {
				$location.path("/response");        						
				$timeout.cancel($rootScope.promise);				
			}			
			return ((ok + notOk +1) / (all +1)) * 100;			
		}
		
	        $rootScope.promise = $timeout($scope.getStatus, 3000);				
	}	
	
	$scope.statisticsEmail = function () {
		$timeout.cancel($rootScope.promise);			
		$location.path("/response");        		
	};
	
	// --------------
	
	// Start the poll	
//	$scope.getStatus();
		
	// Stop the poll
	$scope.$on('$locationChangeStart', function() {
	    $timeout.cancel($rootScope.promise);
	});	
	
	// Stop the poll 2
	$scope.$on('$destroy', function() {
	    $timeout.cancel($rootScope.promise);
	});
	
}]);
