'use strict';

angular.module('email')
.controller('EmailCtrl', ['$scope', '$rootScope', 'EmailService', 'DraftService', 'uploadManager', '$state', 'DialogService', 'Global',
  function($scope, $rootScope, EmailService, DraftService, uploadManager, $state, DialogService, Global) {

    $scope.tinymceOptions = {
      height: 400,
      width: 650,
      menubar: false,
      language: Global.getUserLanguage(),
      //paste plugin to avoid ms word tags and similar content
      plugins: "paste textcolor",
      toolbar: "undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | forecolor backcolor",
      paste_auto_cleanup_on_paste : true,
      paste_remove_styles: true,
      paste_remove_styles_if_webkit: true,
      paste_strip_class_attributes: true
    };

    $scope.email = window.emailData.email;
    $scope.recipients = window.emailData.recipient;
    $scope.email.attachInfo = [];
    $scope.email.html = true;
    $scope.email.from = 'opintopolku@oph.fi'; //For display only, the value comes from the backend configs

    $rootScope.callingProcess = $scope.email.callingProcess;

    $scope.isEmailState = function() {
      return $state.is('email');
    };

    $scope.previewEmail = function() {
      $state.go('email.preview');
    }

    $scope.cancelEmail = function () {
      $state.go('email_cancel');
    };

    $scope.saveDraft = function() {
      var draft = $scope.email;
      if(draft.id) {
        updateDraft(draft);
      } else {
        saveDraft(draft);
      }
    };

    function updateDraft(draft){
      DraftService.drafts.update(draft, function() {
        $state.go('.savedContent.drafts');
      }, function(e) {
        DialogService.showErrorDialog(e.data);
      })

    }

    function saveDraft(draft) {
      DraftService.drafts.save(draft, function() {
        updateDraftCount();
        $state.go('.savedContent.drafts');
      }, function(e) {
        DialogService.showErrorDialog(e.data);
      });
    }

    $scope.counts = {
        drafts : 0,
        emails : 0,
        templates: 0
    };

    $scope.$on('useDraft', function(event, draft) {
      $scope.email = draft;
      $state.go('email');
    });
    $scope.$on('useEmail', function(event, email) {
      $scope.email = email;
      $state.go('email');
    })
    
    $scope.init = function() {
      $scope.initResponse = EmailService.init.query();
      updateDraftCount();
      updateMessageCount();
    };

    $scope.percentage = 0;
    $scope.percentage2 = { width: 0 + '%' }; // For IE9

    $rootScope.$on('uploadProgress', function (e, call) {
      $scope.percentage = call;
      $scope.percentage2 = { width: call + '%' }; // For IE9
      $scope.$apply();
    });

    $rootScope.$on('fileLoaded', function (e, call) {
      $scope.email.attachInfo.push(call);
      $scope.$apply();
    });
    
    $scope.remove = function(id) {
      $scope.email.attachInfo.splice(id, 1);
    };
    
    $scope.init();

    function updateDraftCount() {
      DraftService.drafts.count().$promise.then(function(count){
        $scope.counts.drafts = count.count;
      });
    }

    function updateMessageCount() {
      EmailService.messageCount.get().$promise.then(function(count) {
        $scope.counts.emails = count.count;
      })
    }

  }
]);