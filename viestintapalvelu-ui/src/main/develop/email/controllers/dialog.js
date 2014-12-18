'use strict';

angular.module('email')
.controller('DialogCtrl', ['$scope', '$modalInstance', 'opts', '$filter',
  function($scope, $modalInstance, opts, $filter) {

    var defaultOpts = {
      type: 'info',
      msg: $filter('i18n')('error.unknown'),
      confirm: {
        label: $filter('i18n')('dialog.confirm.header'),
        fn: function() {
          return $filter('i18n')('dialog.ok');
        }
      }
    }

    $scope.opts = angular.isDefined(opts) ? opts : defaultOpts;

    $scope.confirm = function() {
      $modalInstance.close(opts.confirm());
    };

    $scope.cancel = function () {
      $modalInstance.dismiss();
    };

  }
]);