'use strict';

angular.module('email')
.controller('DraftCtrl', ['$scope', 'DraftService',
  function($scope, DraftService) {

    $scope.drafts = [];

    DraftService.query().$promise.then(function(result) {
      $scope.drafts = result;
    });
    
    $scope.selectDraft = function(draft) {
      $scope.$emit('useDraft', draft);
    };
  }
]);