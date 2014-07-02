'use strict';

//--- Resources ---
angular.module('email')
.factory('GroupEmailInitFactory', function($resource) {
  return $resource('/ryhmasahkoposti-service/email/initGroupEmail', {}, {
    initGroupEmail: {method: 'GET', isArray: false}
  });
})

.factory('GroupEmailFactory', function ($resource) {
  return $resource('/ryhmasahkoposti-service/email/sendGroupEmail', {}, {
    sendGroupEmail: { method: 'POST', isArray: false}
  });
})

.factory('EmailSendStatusFactory', function ($resource) {
  return $resource('/ryhmasahkoposti-service/email/sendEmailStatus', {}, {
    sendEmailStatus: { method: 'POST', isArray: false}
  });
})

.factory('EmailResultFactory', function ($resource) {
  return $resource('/ryhmasahkoposti-service/email/sendResult', {}, {
    sendResult: { method: 'POST', isArray: false}
  });
})

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