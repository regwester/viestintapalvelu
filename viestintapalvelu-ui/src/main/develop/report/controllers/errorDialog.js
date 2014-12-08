'use strict';

angular.module('report')
.controller('ErrorDialogCtrl', ['$scope', '$modalInstance', 'msg',
    function ErrorDialogCtrl($scope, $modalInstance, msg) {
        $scope.msg = (angular.isDefined(msg)) ? msg : 'Tuntematon virhe tapahtunut palvelukerroksessa';
        $scope.close = function () {
            $modalInstance.close();
        };
    }
]);