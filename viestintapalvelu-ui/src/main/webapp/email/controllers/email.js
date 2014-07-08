'use strict';

angular.module('email')
.controller('EmailCtrl', ['$scope', '$rootScope', 'EmailService', 'DraftService', 'uploadManager', '$state', 'ErrorDialog', 
  function($scope, $rootScope, EmailService, DraftService, uploadManager, $state, ErrorDialog) {
    $rootScope.emailsendid = "";
    $scope.tinymceOptions = {
      height: 400,
      width: 600,
      menubar: false
    };

    $scope.emaildata = window.emailData;
    $scope.emaildata.email.attachInfo = [];
    $scope.emaildata.email.html = true;

    $scope.sendGroupEmail = function () {
      $scope.emailsendid = EmailService.email.save($scope.emaildata).$promise.then(
        function(value) {
          $rootScope.emailsendid = value;
          $state.go('email_status');
        },
        function(error) {
          ErrorDialog.showError(error);
        },
        function(update) {
          alert("Notification " + update);
        }
      );
    };
    
    $scope.isEmailState = function() {
      return $state.is('email');
    };
    
    $scope.cancelEmail = function () {
      $state.go('email_cancel');
      $rootScope.callingProcess = $scope.emaildata.email.callingProcess;
    };

    $scope.saveDraft = function() {
      DraftService.save($scope.emaildata.email, function(id) {
        //success
        $state.go('drafts');
      }, function(e) {
        //error
        //do the error dialog popup
      });
    };

    $scope.init = function() {
      $scope.initResponse = EmailService.init.query();
    };

    $scope.percentage = 0;
    $scope.percentage2 = { width: 0 + '%' }; // For IE9

    $rootScope.$on('uploadProgress', function (e, call) {
      $scope.percentage = call;
      $scope.percentage2 = { width: call + '%' }; // For IE9
      $scope.$apply();
    });

    $rootScope.$on('fileLoaded', function (e, call) {
      $scope.emaildata.email.attachInfo.push(call);
      $scope.$apply();
    });

    //--- 'Poista' ---
    $scope.remove = function(id) {
      $scope.emaildata.email.attachInfo.splice(id, 1);
    };
    
    $scope.showRecipients = function() {
      return $scope.emaildata.recipient.length <= 30;
    };
    
    $scope.init();
  }
]);