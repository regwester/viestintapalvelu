'use strict';

reportingApp.controller('ReportedMessageViewController', 
	function ReportedMessageViewController($scope, $routeParams, $location, GetReportedMessagesBySearchArgument, 
		ReportedMessage) {
		$scope.reportedMessage = ReportedMessage.get({messageID: $routeParams.messageID});
	
		$scope.back = function() {
			$location.path('/reportMessages/list');
		};
});