'use strict';

// Hakee REST-rajapinnan avulla raportoitavan viestin ja vastaanottajat sivutettuna ja lajiteltuna
angular.module('report')
.factory('ReportedMessageAndRecipients', ['$resource', function($resource) {
    return $resource(window.url("ryhmasahkoposti-service.report.vwp"), {messageID: '@messageID'});
}])

// Hakee REST-rajapinnan avulla raportoitavan viestin tiedot vastaanottajineen, joille viestin lähetys epäonnistui
.factory('ReportedMessageAndRecipientsSendingUnsuccessful', ['$resource', function($resource) {
    return $resource(window.url("ryhmasahkoposti-service.report.failed"), {messageID: '@messageID'});
}])

// Hakee REST-rajapinnan avulla raportoitavan viestin tiedot vastaanottajineen, joille viestin lähetys palautui
.factory('ReportedMessageAndRecipientsSendingBounced', ['$resource', function($resource) {
  return $resource(window.url("ryhmasahkoposti-service.report.bounced"), {messageID: '@messageID'});
}])

  // Hakee REST-rajapinnan avulla hakuparametrin mukaiset raportoitavat viestit
.factory('GetReportedMessagesBySearchArgument', ['$resource', function($resource) {
    return $resource(window.url("ryhmasahkoposti-service.report.search"), {}, {
        get: {method: "GET", isArray: false}
    });
}])

//Hakee REST-rajapinnan avulla organisaation raportoitavat viestit 
.factory('GetReportedMessagesByOrganization', ['$resource', function($resource) {
    return $resource(window.url("ryhmasahkoposti-service.report.list"), {}, {
        get: {method: "GET", isArray: false}
    });
}])

// Hae raportoitava viesti ja vastaanottajat
.factory('GetReportedMessageAndRecipients', ['$route', '$q', 'ReportedMessageAndRecipients',
        function($route, $q, ReportedMessageAndRecipients) {
    return function() {
        var delay = $q.defer();
        ReportedMessageAndRecipients.get({messageID: $route.current.params.messageID}, function(reportedMessage) {
          delay.resolve(reportedMessage);
        }, function() {
          delay.reject('Unable to get reported message ' + $route.current.params.messageID);
        });
        return delay.promise;
    };
}])

// Hae raportoitava viesti ja vastaanottajat, joille lähetys on epäonnistunut
.factory('GetReportedMessageAndRecipientsSendingUnsuccessful', ['$route', '$q', 'ReportedMessageAndRecipientsSendingUnsuccessful',
        function($route, $q, ReportedMessageAndRecipientsSendingUnsuccessful) {
    return function() {
        var delay = $q.defer();
        ReportedMessageAndRecipientsSendingUnsuccessful.get({messageID: $route.current.params.messageID}, function(reportedMessage) {
          delay.resolve(reportedMessage);
        }, function() {
          delay.reject('Unable to get reported message ' + $route.current.params.messageID);
        });
        return delay.promise;
    };
}])

// Hae raportoitava viesti ja vastaanottajat, joille lähetys on palautunut
.factory('GetReportedMessageAndRecipientsSendingBounced', ['$route', '$q', 'ReportedMessageAndRecipientsSendingBounced',
  function($route, $q, ReportedMessageAndRecipientsSendingBounced) {
    return function() {
      var delay = $q.defer();
      ReportedMessageAndRecipientsSendingBounced.get({messageID: $route.current.params.messageID}, function(reportedMessage) {
        delay.resolve(reportedMessage);
      }, function() {
        delay.reject('Unable to get reported message ' + $route.current.params.messageID);
      });
      return delay.promise;
    };
}]);
