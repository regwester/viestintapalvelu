'use strict';

reportingApp.factory('ReportedMessage', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/view/:messageID", {messageID: '@messageID'});
});

reportingApp.factory('GetReportedMessages', function($resource) {
    return $resource("/ryhmasahkoposti-service/reportMessages/list", {}, {
    	get: {method: "GET", isArray: true}
    });	
});

reportingApp.factory('GetReportedMessagesBySearchArgument', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/search/:searchArgument", {}, {
		get: {method: "GET", params: {searchArgument: '@searchArgument'}, isArray: true}
    });
});

reportingApp.factory('GetReportedMessage', function($route, $q, ReportedMessage) {
	return function() {
	    var delay = $q.defer();
	    ReportedMessage.get({messageID: $route.current.params.messageID}, function(reportedMessage) {
	      delay.resolve(reportedMessage);
	    }, function() {
	      delay.reject('Unable to get reportted message '  + $route.current.params.messageID);
	    });
	    return delay.promise;		
	};
});
