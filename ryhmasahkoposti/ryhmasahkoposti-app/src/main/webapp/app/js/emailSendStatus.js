
var emailStatus = angular.module('viestintapalvelu');

emailStatus.controller('EmailSendStatusController', ['$scope', '$rootScope', 'EmailSendStatusFactory', '$location', '$timeout', 
                                                     function($scope, $rootScope, EmailSendStatusFactory, $location, $timeout) { 	

	$scope.percentage  = 0;		
	$scope.emailsendid = $rootScope.emailsendid;

	$scope.getStatus = function () {
//		alert("$scope.getStatus");
		
		$scope.LahetyksenTilanneDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id);	
	
		var ok = $scope.LahetyksenTilanneDTO.lahetysOnnistuiLukumaara;
		var notOk = $scope.LahetyksenTilanneDTO.lahetysEpaonnistuiLukumaara;
		var all = $scope.LahetyksenTilanneDTO.vastaanottajienLukumaara;
		
		$scope.percentage = parseInt((ok + notOk) / all * 100, 10);    		
		
		if (all == ok + notOk) {
			$location.path("/response");        			
			
			$timeout.cancel($rootScope.promise);	
		}

        $rootScope.promise = $timeout($scope.getStatus, 3000);	
	}	
	
	$scope.updateStatus = function () {
		$location.path("/status");        		
	};
	
	
	$scope.statisticsEmail = function () {
		$timeout.cancel($rootScope.promise);	
		
		$location.path("/response");        		
	};
	
	// --------------
	
	// Start the poll	
//	$scope.getStatus();
		
//	// Stop the poll
//	$scope.$on('$locationChangeStart', function() {
//	    $timeout.cancel($rootScope.promise);
//	});	
//	
//	// Stop the poll 2
//	$scope.$on('$destroy', function() {
//	    $timeout.cancel($rootScope.promise);
//	});
	
}]);
