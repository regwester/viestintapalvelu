'use strict';

angular.module('email')
  .controller('PreviewCtrl', ['$scope', 'EmailService', '$state', 'DialogService', 'DraftService',
    function($scope, EmailService, $state, DialogService, DraftService) {

      $scope.sendEmail = function () {
        $scope.email.callingProcess = $scope.callingProcess; //TODO: check this actually works, it should dou
        EmailService.email.save({recipient: $scope.recipients, email: $scope.email}).$promise.then(
          function(resp) {
            DraftService.selectedDraft() && DraftService.drafts.delete({id: selectedDraft});
            $state.go('report_view', {messageID: resp.id});
          },
          function(e) {
            DialogService.showErrorDialog(e.data);
          }
        );
      };

    }
  ]);