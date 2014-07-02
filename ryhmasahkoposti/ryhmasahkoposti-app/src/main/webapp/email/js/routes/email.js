'use strict';

angular.module('viestintapalvelu')
.config(['$routeProvider',
  function ($routeProvider) {
    $routeProvider.when('/email', {templateUrl: '/ryhmasahkoposti-app/email/html/email.html', controller: 'EmailCtrl'});
    $routeProvider.when('/cancel/', {templateUrl: '/ryhmasahkoposti-app/email/html/emailCancel.html', controller: 'EmailCancelCtrl'});
    $routeProvider.when('/status/', {templateUrl: '/ryhmasahkoposti-app/email/html/emailSendStatus.html', controller: 'EmailSendStatusCtrl'});
    $routeProvider.when('/response/', {templateUrl: '/ryhmasahkoposti-app/email/html/emailResponse.html', controller: 'EmailResponseCtrl'});
    $routeProvider.otherwise({redirectTo: '/email'});
  }
]);