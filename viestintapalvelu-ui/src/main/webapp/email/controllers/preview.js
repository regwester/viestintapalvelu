'use strict';

angular.module('email')
  .controller('PreviewCtrl', ['$scope', 'EmailService', '$state', 'ErrorDialog',
    function($scope, EmailService, $state, ErrorDialog) {
      $scope.sendGroupEmail = function () {
        $scope.email.callingProcess = $scope.callingProcess; //TODO: check this actually works, it should dou
        EmailService.email.save({recipient: $scope.recipients, email: $scope.email}).$promise.then(
          function(resp) {
            var selectedDraft = DraftService.selectedDraft();
            if(!!selectedDraft) {
              DraftService.drafts.delete({id: selectedDraft});
            }
            $state.go('report_view', {messageID: resp.id});
          },
          function(e) {
            ErrorDialog.showError(e);
          }
        );
      };

    }
  ]);