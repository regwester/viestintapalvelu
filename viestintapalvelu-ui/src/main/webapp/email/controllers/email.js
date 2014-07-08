'use strict';

angular.module('email')
.controller('EmailCtrl', ['$scope', '$rootScope', 'GroupEmail', 'uploadManager', '$state', 'ErrorDialog', 
  function($scope, $rootScope, GroupEmail, uploadManager, $state, ErrorDialog) {
    $rootScope.emailsendid = "";
    $scope.tinymceOptions = {
      height: 400,
      width: 600,
      menubar: false
    };

    $scope.emaildata = window.emailData;
    $scope.emaildata.email.attachInfo = [];

    $scope.sendGroupEmail = function () {
      $scope.emailsendid = GroupEmail.email.save($scope.emaildata).$promise.then(
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

    $scope.cancelEmail = function () {
      $state.go('email_cancel');
      $rootScope.callingProcess = $scope.emaildata.email.callingProcess;
    };

    $scope.init = function() {
      $scope.initResponse = GroupEmail.init.query();
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