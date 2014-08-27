'use strict';

angular.module('email')
.controller('TabCtrl', ['$scope', '$state', function($scope, $state) {
  
  //$scope.tabs = [{active: false},{active: false},{active:false}];
  $scope.tabs = [{active: false}, {active: false}];
  $scope.selectActiveTab = function() {
    //var states = ['email.savedContent.templates', 'email.savedContent.sentMails', 'email.savedContent.drafts'];
    var states = ['email.savedContent.sentMails', 'email.savedContent.drafts'];
    var index = states.indexOf($state.current.name);
    index = (index != -1) ? index : 0;
    $scope.tabs[index].active = true;
  };
  
}]);