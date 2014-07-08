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
    
    //Change these to substates (use dot notation, change url and configure controllers)
    .state('email_cancel', {
      url: '/email/cancel',
      templateUrl: emailUrl + 'emailCancel.html',
      controller: 'EmailCancelCtrl'
    }).state('email_status', {
      url: '/email/status',
      templateUrl: emailUrl + 'emailSendStatus.html',
      controller: 'EmailSendStatusCtrl'
    }).state('email_response', {
      url: '/email/response',
      controller: 'EmailResponseCtrl',
      templateUrl: emailUrl + 'emailResponse.html'
    })
    
    .state('report', { //same here
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
    });
  }
]);