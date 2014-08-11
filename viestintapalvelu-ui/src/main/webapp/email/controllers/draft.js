'use strict';

angular.module('email')
.controller('DraftCtrl', ['$scope', '$filter', 'DraftService',
  function($scope, $filter, DraftService) {
    $scope.drafts = [];
    $scope.pagedDrafts = [];
    DraftService.query().$promise.then(function(result) {
      $scope.drafts = result;
      groupToPages();
    });
    
    $scope.useDraft = function(draft) {
      $scope.$emit('useDraft', draft);
    };
    
    $scope.itemsPerPage = 10;
    $scope.totalItems = function() {
      return $scope.drafts.length;
    };
    $scope.currentPage = 1;
    $scope.pageChanged = function() {
      console.log('Current Page: ' + $scope.currentPage);
    };

    function groupToPages() {
      $scope.pagedDrafts = [];
      for (var i = 0; i < $scope.drafts.length; i++) {
        if (i % $scope.itemsPerPage === 0) {
            $scope.pagedDrafts[Math.floor(i / $scope.itemsPerPage)] = [$scope.drafts[i]];
        } else {
            $scope.pagedDrafts[Math.floor(i / $scope.itemsPerPage)].push($scope.drafts[i]);
        }
      }
    }
    
    //Temporary workaround until angular ui tooltip supports templates
    $scope.getAttachmentsAsHtml = function(draft) {
      var attachments = draft.attachInfo;
      var html = '';
      for(var i = 0; i < attachments.length; i++) {
        html += '<span>' + attachments[i].fileName + '</span>';
        html += '&nbsp;';
        html += '<span>(' + $filter('bytesToSize')(attachments[i].fileSize) + ')</span>';
        html += '<br/>';
      }
      return html;
    };
    
  }
]);