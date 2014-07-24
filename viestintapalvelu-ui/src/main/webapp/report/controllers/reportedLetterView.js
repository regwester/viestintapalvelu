'use strict';

angular.module('report')
.controller('ReportedLetterViewCtrl', ['$scope', '$state','$stateParams', function ($scope, $state, $stateParams) {

  $scope.back = function() {
    $state.go('letter_reports');
  }

}]);
