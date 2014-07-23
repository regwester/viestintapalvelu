'use strict';

angular.module('viestintapalvelu')
.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/email');
    
    var emailUrl = '/viestintapalvelu-ui/email/views/';
    var reportUrl = '/viestintapalvelu-ui/report/views/';
    
    $stateProvider
    .state('email', {
      url: '/email',
      templateUrl: emailUrl + 'email.html',
      controller: 'EmailCtrl'
    })
      .state('email.savedContent', {
        url: '',
        abstract: true,
        templateUrl: emailUrl + 'savedContent.html',
        controller: 'TabCtrl'
      })
        .state('email.savedContent.drafts', {
          url: '/drafts',
          templateUrl: emailUrl + 'partials/drafts.html',
          controller: 'DraftCtrl'
        })
        .state('email.savedContent.sentEmails', {
          url: '/sentEmails',
          templateUrl: emailUrl + 'partials/sentEmails.html'
        })
        .state('email.savedContent.templates', {
          url: '/templates',
          templateUrl: emailUrl + 'partials/templates.html'
        })
    
    .state('email_cancel', {
      url: '/email/cancel',
      templateUrl: emailUrl + 'emailCancel.html',
      controller: 'EmailCancelCtrl'
    })    
    .state('report', { //Change these to substates (use dot notation, change url and configure controllers)
      url: '/reportMessages/list',
      templateUrl: reportUrl + 'reportedMessageList.html',
      controller: 'ReportedMessageListCtrl'
    }).state('report_search', {
      url: '/reportMessages/search',
      templateUrl: reportUrl + 'reportedMessageList.html',
      controller: 'ReportedMessageListCtrl'
    }).state('report_view', {
      url: '/reportMessages/view/:messageID',
      templateUrl: reportUrl + 'reportedMessageView.html',
      controller: 'ReportedMessageViewCtrl'
    })

    .state('letter_reports', {
      url: '/reportLetters',
      templateUrl: reportUrl + 'reportedLetterList.html',
      controller: 'ReportedLetterListCtrl'
    });
  }
]);