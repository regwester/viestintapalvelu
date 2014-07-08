'use strict';

angular.module('email')
.controller('EmailResponseCtrl', ['$scope', '$rootScope', 'EmailService', 'ErrorDialog',
  function($scope, $rootScope, EmailService, ErrorDialog) {
    $scope.tinymceOptions = {
      readonly : 1,
      height: 400,
      width: 600
    };

    $scope.emailsendid = $rootScope.emailsendid;
    $scope.ReportedMessageDTO = EmailService.result.save($scope.emailsendid.id).$promise.then(
      function(value) {
        $scope.ReportedMessageDTO = value; 
        $scope.showTo  = $scope.ReportedMessageDTO.emailRecipients.length <= 30;
        $scope.showCnt = $scope.ReportedMessageDTO.emailRecipients.length >  30;
        $scope.recipCnt = $scope.ReportedMessageDTO.emailRecipients.length + " vastaanottajaa";
      },
      function(error) {
        ErrorDialog.showError(error);
      },
      function(update) {
        alert("Notification " + update);
      }
    );

    $scope.SendingStatusDTO = EmailService.status.save($scope.emailsendid.id).$promise.then(
      function(value) {
        $scope.SendingStatusDTO = value;
      }, function(error) {
        ErrorDialog.showError(error);
      }, function(update) {
        alert("Notification " + update);
      }
    );

  }
]);