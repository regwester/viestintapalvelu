'use strict';

reportingApp.controller('ReportedMessageListController',
	function ReportedMessageListController($scope, $location, GetReportedMessages, GetReportedMessagesBySearchArgument, reportedMessages) {
	    $scope.reportedMessages = reportedMessages;
	
	    $scope.search = function(searchArgument) {
	    	$scope.reportedMessages = GetReportedMessagesBySearchArgument.get({searchArgument: searchArgument});
	    };
	
	    $scope.clean = function() {
	        $scope.searchArgument = '';
	        $scope.reportedMessages = GetReportedMessages.get();
	    };
});
