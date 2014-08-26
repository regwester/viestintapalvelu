'use strict';

angular.module('email')
.factory('EmailService', ['$resource', function($resource) {
  var baseUrl = '/ryhmasahkoposti-service/email/';
  return {
    init: $resource(baseUrl + 'ok', {}, { 
      query: {method: 'GET', isArray: false}
    }),
    status: $resource(baseUrl + 'status'),
    email: $resource(baseUrl + ''),
    result: $resource(baseUrl + 'result'),
    messages: $resource('/ryhmasahkoposti-service/reportMessages/currentUserHistory'),
    messageCount: $resource(baseUrl + 'count')
  };
}]);
