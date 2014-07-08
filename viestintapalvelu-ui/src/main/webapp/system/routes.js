'use strict';

angular.module('viestintapalvelu')
.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/email');

    $stateProvider
    .state('email', { 
      url: "/email",
      templateUrl: "/viestintapalvelu-ui/email/views/email.html",
      controller: "EmailCtrl"
    }).state('email.draft', {
      url: '/draft',
      templateUrl: "/viestintapalvelu-ui/email/views/draft.html",
      controller: "DraftCtrl"
    })
    //Change these to substates (use dot notation, change url and configure controllers)
    .state('email_cancel', {
      url: "/email/cancel",
      templateUrl: "/viestintapalvelu-ui/email/views/emailCancel.html",
      controller: "EmailCancelCtrl"
    }).state('email_status', {
      url: "/email/status",
      templateUrl: "/viestintapalvelu-ui/email/views/emailSendStatus.html",
      controller: "EmailSendStatusCtrl"
    }).state('email_response', {
      url: "/email/response",
      controller: "EmailResponseCtrl",
      templateUrl: "/viestintapalvelu-ui/email/views/emailResponse.html"
    })

    .state('report', { //same here
      url: "/reportMessages/list",
      templateUrl:"/viestintapalvelu-ui/report/views/reportedMessageList.html",
      controller: "ReportedMessageListCtrl"
    }).state('report_search', {
      url: "/reportMessages/search",
      templateUrl:"/viestintapalvelu-ui/report/views/reportedMessageList.html",
      controller: "ReportedMessageListCtrl"
    }).state('report_view', {
      url: "/reportMessages/view/:messageID",
      templateUrl: "/viestintapalvelu-ui/report/views/reportedMessageView.html",
      controller: "ReportedMessageViewCtrl"
    });
  }
]);