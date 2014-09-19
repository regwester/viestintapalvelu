'use strict';

angular.module('email')
  .controller('PreviewCtrl', ['$scope', 'EmailService', '$state', 'DialogService', 'DraftService',
    function($scope, EmailService, $state, DialogService, DraftService) {
      $scope.sendingInProgress = false;

      $scope.downloadSupported = function() {
        return !!new Blob;
      };

      $scope.downloadEmail = function() {
        EmailService.preview($scope.getEmailData())
          .success(function(res) {
            var blob = new Blob([res], {type: "text/plain;charset=utf-8"});
            saveAs(blob, "preview.eml");
          })
          .error(function(res, code) {
            DialogService.showErrorDialog(res.data);
          });
      };

      $scope.sendEmail = function () {
        if (!$scope.sendingInProgress) {
            $scope.sendingInProgress = true;
            $scope.email.callingProcess = $scope.callingProcess;
            EmailService.email.save($scope.getEmailData()).$promise.then(
                function(res) {
                    DraftService.selectedDraft() && DraftService.drafts.delete({id: selectedDraft});
                    $state.go('report_view', {messageID: res.id});
                    $scope.sendingInProgress = false;
                }, function(e) {
                    DialogService.showErrorDialog(e.data);
                    $scope.sendingInProgress = false;
                }
            );
        }
      };

    }
  ]);