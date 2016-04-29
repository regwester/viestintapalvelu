'use strict';

angular.module('email')
.factory('EmailService', ['$resource', '$http', function($resource, $http) {
  return {
    init: $resource(window.url("ryhmasahkoposti-service.resource", "ok"), {}, { 
      query: {method: 'GET', isArray: false}
    }),
    status: $resource(window.url("ryhmasahkoposti-service.resource", "status")),
    email: $resource(window.url("ryhmasahkoposti-service.resource","")),
    preview: function(emailData){
      return $http.post(window.url("ryhmasahkoposti-service.preview"), emailData);
    },
    result: $resource(window.url("ryhmasahkoposti-service.resource","result")),
    messages: $resource(window.url("ryhmasahkoposti-service.report.currentUserHistory")),
    messageCount: $resource(window.url("ryhmasahkoposti-service.resource","count"))
  };
}]);
