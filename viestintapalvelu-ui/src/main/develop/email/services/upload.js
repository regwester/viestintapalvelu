'use strict';

angular.module('email')
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