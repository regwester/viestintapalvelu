'use strict';

reportingApp.factory('ReportedMessageAndRecipients', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/vwp/:messageID", {messageID: '@messageID'});
});

reportingApp.factory('ReportedMessageAndRecipientsSendingUnsuccesful', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/failed/:messageID", {messageID: '@messageID'});
});

reportingApp.factory('GetReportedMessages', function($resource) {
    return $resource("/ryhmasahkoposti-service/reportMessages/list", {}, {
    	get: {method: "GET", isArray: false}
    });	
});

reportingApp.factory('GetReportedMessagesBySearchArgument', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/search/:searchArgument", {}, {
		get: {method: "GET", params: {searchArgument: '@searchArgument'}, isArray: false}
    });
});

reportingApp.factory('GetReportedMessageAndRecipients', function($route, $q, ReportedMessageAndRecipients) {
	return function() {
	    var delay = $q.defer();
	    ReportedMessageAndRecipients.get({messageID: $route.current.params.messageID}, function(reportedMessage) {
	      delay.resolve(reportedMessage);
	    }, function() {
	      delay.reject('Unable to get reportted message '  + $route.current.params.messageID);
	    });
	    return delay.promise;		
	};
});

reportingApp.factory('GetReportedMessageAndRecipientsSendingUnsuccesful', function($route, $q, 
	ReportedMessageAndRecipientsSendingUnsuccesful) {
	return function() {
	    var delay = $q.defer();
	    ReportedMessageAndRecipientsSendingUnsuccesful.get({messageID: $route.current.params.messageID}, function(reportedMessage) {
	      delay.resolve(reportedMessage);
	    }, function() {
	      delay.reject('Unable to get reportted message '  + $route.current.params.messageID);
	    });
	    return delay.promise;		
	};
});
