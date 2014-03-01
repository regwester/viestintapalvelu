'use strict';

reportingApp.controller('ReportedMessageListController',
	function ReportedMessageListController($scope, $location, GetReportedMessages, GetReportedMessagesBySearchArgument, 
		SharedVariables, reportedMessages) {
		if (SharedVariables.getSearchArgumentValue() == '') {
		    $scope.reportedMessages = reportedMessages;			
		} else {
			$scope.searchArgument = SharedVariables.getSearchArgumentValue();
	    	$scope.reportedMessages = GetReportedMessagesBySearchArgument.get({searchArgument: SharedVariables.getSearchArgumentValue()});
		}
	
	    $scope.search = function(searchArgument) {
	    	SharedVariables.setSearchArgumentValue(searchArgument);
	    	$scope.reportedMessages = GetReportedMessagesBySearchArgument.get({searchArgument: searchArgument});
	    };
	
	    $scope.clean = function() {
	    	SharedVariables.setSearchArgumentValue('');
	        $scope.searchArgument = '';
	        $scope.reportedMessages = GetReportedMessages.get();
	    };
});
