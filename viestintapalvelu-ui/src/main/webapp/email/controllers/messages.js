'use strict';

angular.module('email')
  .controller('MessagesCtrl', ['$scope', 'EmailService',
    function($scope, EmailService) {

      $scope.messages = [];

      EmailService.messages.get(function(result) {
        $scope.messages = result.reportedMessages;
      });

      $scope.selectEmail = function(email) {
        $scope.$emit('useEmail', email);
      };
    }
  ]);