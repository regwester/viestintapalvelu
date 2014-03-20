'use strict';

// Hakee REST-rajapinnan avulla raportoitavan viestin ja vastaanottajat sivutettuna ja lajiteltuna
reportingApp.factory('ReportedMessageAndRecipients', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/vwp/:messageID", {messageID: '@messageID'});
});

// Hakee REST-rajapinnan avulla raportoitavan viestin tiedot vastaanottajineen, joille viestin lähetys epäonnistui
reportingApp.factory('ReportedMessageAndRecipientsSendingUnsuccesful', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/failed/:messageID", {messageID: '@messageID'});
});

// Hakee REST-rajapinnan avulla kaikki raportoitavat viestit
reportingApp.factory('GetReportedMessages', function($resource) {
    return $resource("/ryhmasahkoposti-service/reportMessages/list", {}, {
    	get: {method: "GET", isArray: false}
    });	
});

// Hakee REST-rajapinnan avulla hakuparametrin mukaiset raportoitavat viestit 
reportingApp.factory('GetReportedMessagesBySearchArgument', function($resource) {
	return $resource("/ryhmasahkoposti-service/reportMessages/search/:searchArgument", {}, {
		get: {method: "GET", params: {searchArgument: '@searchArgument'}, isArray: false}
    });
});

// Hae raportoitava viesti ja vastaanottajat
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

// Hae raportoitava viesti ja vastaanottajat, joille lähetys on epäonnistunut
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
