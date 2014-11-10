'use strict';

angular.module('letter-templates')
    .controller('TemplateDialogCtrl', ['$scope', '$modalInstance',
        function($scope, $modalInstance) {
            $scope.confirm = function() {
                $modalInstance.close();
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }
    ]);