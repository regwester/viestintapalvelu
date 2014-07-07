'use strict';

//--- Resources ---
angular.module('email')
.factory('GroupEmail', ['$resource', function($resource){
  var baseUrl = '/ryhmasahkoposti-service/email/';
  return {
    init: $resource(baseUrl + 'initGroupEmail', {}, { //These endpoints are bad. There should never be verbs in the url.
      query: {method: 'GET', isArray: false}
    }),
    status: $resource(baseUrl + 'sendEmailStatus'),
    email: $resource(baseUrl + 'sendGroupEmail'),
    result: $resource(baseUrl + 'sendResult')
  };
}])

//--- Upload ---
.factory('uploadManager', function ($rootScope) {
  return {
    add: function (file) {
      file.submit();
    },
    setProgress: function (percentage) {
      $rootScope.$broadcast('uploadProgress', percentage);
    },
    setResult : function(result) {
      $rootScope.$broadcast('fileLoaded', result);
    }
  };
});