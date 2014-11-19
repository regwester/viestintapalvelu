'use strict';

angular.module('email')
.controller('EmailCancelCtrl', ['$scope', '$rootScope', '$location',
  function($scope, $rootScope, $location) {
    $scope.callingProcess = $rootScope.callingProcess;
  }
]);