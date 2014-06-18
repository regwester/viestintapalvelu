'use strict';

angular.module('viestintapalvelu')
.controller('EmailResponseCtrl', ['$scope', '$rootScope', 'EmailResultFactory', 'EmailSendStatusFactory', '$location', 'ErrorDialog',
  function($scope, $rootScope, EmailResultFactory, EmailSendStatusFactory, $location, ErrorDialog) {
    $scope.tinymceOptions = {
      readonly : 1,
      height: 400,
      width: 600
    };

    $scope.emailsendid = $rootScope.emailsendid;
    $scope.ReportedMessageDTO = EmailResultFactory.sendResult($scope.emailsendid.id).$promise.then(
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

    $scope.SendingStatusDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id).$promise.then(
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