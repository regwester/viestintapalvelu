'use strict';

angular.module('viestintapalvelu')
.controller('EmailCancelCtrl', ['$scope', '$rootScope', '$location',
  function($scope, $rootScope, $location) {
    $scope.callingProcess = $rootScope.callingProcess;
  }
]);