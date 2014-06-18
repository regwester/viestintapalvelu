'use strict';

angular.module('viestintapalvelu')
.controller('ErrorDialogCtrl', ['$scope', '$modalInstance', 'msg',
  function($scope, $modalInstance, msg) {
    $scope.msg = (angular.isDefined(msg)) ? msg : 'Tuntematon virhe tapahtunut palvelukerroksessa';
    $scope.close = function () {
      $modalInstance.close();
    };
  }
]);