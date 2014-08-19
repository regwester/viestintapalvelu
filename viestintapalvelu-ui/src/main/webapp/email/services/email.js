'use strict';

angular.module('email')
.factory('EmailService', ['$resource', function($resource) {
  var baseUrl = '/ryhmasahkoposti-service/email/';
  return {
    init: $resource(baseUrl + 'initGroupEmail', {}, { //These endpoints are bad. There should never be verbs in the url.
      query: {method: 'GET', isArray: false}
    }),
    status: $resource(baseUrl + 'sendEmailStatus'),
    email: $resource(baseUrl + 'sendGroupEmail'),
    result: $resource(baseUrl + 'sendResult'),
    messages: $resource('/ryhmasahkoposti-service/reportMessages/currentUserHistory'),
    messageCount: $resource(baseUrl + 'count')
  };
}]);
