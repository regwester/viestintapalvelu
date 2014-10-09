'use strict';

angular.module('email')
.factory('EmailService', ['$resource', '$http', function($resource, $http) {
  var baseUrl = '/ryhmasahkoposti-service/email/';
  return {
    init: $resource(baseUrl + 'ok', {}, { 
      query: {method: 'GET', isArray: false}
    }),
    status: $resource(baseUrl + 'status'),
    email: $resource(baseUrl),
    preview: function(emailData){
      return $http.post('/ryhmasahkoposti-service/email/preview/', emailData);
    },
    result: $resource(baseUrl + 'result'),
    messages: $resource('/ryhmasahkoposti-service/reportMessages/currentUserHistory'),
    messageCount: $resource(baseUrl + 'count')
  };
}]);
