'use strict';

angular.module('email')
.controller('DraftCtrl', ['$scope', 'DraftService',
  function($scope, DraftService) {

    $scope.drafts = [];

    DraftService.drafts.query().$promise.then(function(result) {
      $scope.drafts = result;
    });
    
    $scope.selectDraft = function(draft) {
      DraftService.selectDraft(draft.id);
      $scope.$emit('useDraft', draft);
    };
  }
]);