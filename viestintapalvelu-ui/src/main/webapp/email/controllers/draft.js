'use strict';

angular.module('email')
.controller('DraftCtrl', ['$scope', '$state', 'DraftService',
  function($scope, $state, DraftService) {
    $scope.drafts = DraftService.query();
  }
]);