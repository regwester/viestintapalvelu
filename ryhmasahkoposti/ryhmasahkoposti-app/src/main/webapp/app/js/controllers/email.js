'use strict';

angular.module('viestintapalvelu')
.controller('EmailCtrl', ['$scope', '$rootScope', 'GroupEmailInitFactory', 'GroupEmailFactory' ,'uploadManager', '$location', 'ErrorDialog', 
  function($scope, $rootScope, GroupEmailInitFactory, GroupEmailFactory, uploadManager, $location, ErrorDialog) {
    $rootScope.emailsendid = "";
    $scope.tinymceOptions = {
      height: 400,
      width: 600
    };

    // Create empty email to get the attachInfo[] to the object
    $scope.email = 
      {callingProcess: '',
      from: '',
      organizationOid: '',
      replyTo: '',
      subject: '',
      body: '',
      attachInfo: []
    };

    $scope.emaildata = window.emailData;
    
    // Copy the received email values to the empty email 
    $scope.email.callingProcess = $scope.emaildata.email.callingProcess;
    $scope.email.from = $scope.emaildata.email.from;
    $scope.email.organizationOid = $scope.emaildata.email.organizationOid;
    $scope.email.replyTo = $scope.emaildata.email.replyTo;
    $scope.email.subject = $scope.emaildata.email.subject;
    $scope.email.body = $scope.emaildata.email.body;

    $scope.email.templateName = $scope.emaildata.email.templateName;
    $scope.email.languageCode = $scope.emaildata.email.languageCode;

    // Copy to emaildata.email the original sended values WITH the empty attachInfo[]
    $scope.emaildata.email = $scope.email; 

    $scope.showTo  = $scope.emaildata.recipient.length <= 30;
    $scope.showCnt = $scope.emaildata.recipient.length >  30;

    $scope.sendGroupEmail = function () {
      $scope.emailsendid = GroupEmailFactory.sendGroupEmail($scope.emaildata).$promise.then(
        function(value) {
          $rootScope.emailsendid = value;
          $location.path("/status");
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
      $location.path("/cancel");
      $rootScope.callingProcess = $scope.emaildata.email.callingProcess;
    };

    $scope.init = function() {
      $scope.initResponse = GroupEmailInitFactory.initGroupEmail();
    };

    $scope.files = [];
    $scope.percentage = 0;
    $scope.percentage2 = { width: 0 + '%' }; // For IE9
    // Upload --> 
    $scope.upload = function () {
      uploadManager.upload();
      $scope.files = [];
    };

    $rootScope.$on('fileAdded', function (e, call) {
      $scope.files.push(call);
      $scope.$apply();
    });

    $rootScope.$on('uploadProgress', function (e, call) {
      $scope.percentage = call;
      $scope.percentage2 = { width: call + '%' }; // For IE9
      $scope.$apply();
    });

    $rootScope.$on('uploadDone', function (e, call) {
      $scope.$apply();
    });
      
    $rootScope.$on('fileLoaded', function (e, call) {
      $scope.emaildata.email.attachInfo.push(call);
      $scope.$apply();
    });

    //--- 'Poista' ---
    $scope.remove = function(id) {
      //alert("Poista " + id);
      $scope.emaildata.email.attachInfo.splice(id, 1);
    };

    $scope.init();
  }
]);