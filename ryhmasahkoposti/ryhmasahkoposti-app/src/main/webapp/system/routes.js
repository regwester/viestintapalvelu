'use strict';

angular.module('viestintapalvelu')
.config(['$stateProvider', '$urlRouterProvider',
  function ($stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise('/email');

    $stateProvider
    .state('email', { //Change these to substates (use dot notation, change url and configure controllers)
      url: "/email",
      templateUrl: "/ryhmasahkoposti-app/email/views/email.html",
      controller: "EmailCtrl"
    }).state('email_cancel', {
      url: "/email/cancel",
      templateUrl: "/ryhmasahkoposti-app/email/views/emailCancel.html",
      controller: "EmailCancelCtrl"
    }).state('email_status', {
      url: "/email/status",
      templateUrl: "/ryhmasahkoposti-app/email/views/emailSendStatus.html",
      controller: "EmailSendStatusCtrl"
    }).state('email_response', {
      url: "/email/response",
      controller: "EmailResponseCtrl",
      templateUrl: "/ryhmasahkoposti-app/email/views/emailResponse.html"
    })

    .state('report', { //same here
      url: "/reportMessages/list",
      templateUrl:"/ryhmasahkoposti-app/report/views/reportedMessageList.html",
      controller: "ReportedMessageListCtrl"
    }).state('report_search', {
      url: "/reportMessages/search",
      templateUrl:"/ryhmasahkoposti-app/report/views/reportedMessageList.html",
      controller: "ReportedMessageListCtrl"
    }).state('report_view', {
      url: "/reportMessages/view/:messageID",
      templateUrl: "/ryhmasahkoposti-app/report/views/reportedMessageView.html",
      controller: "ReportedMessageViewCtrl"
    });
  }
]);