'use strict';


angular.module('reportingApp')
.config(function ($routeProvider) {
    $routeProvider.
    when('/reportMessages/list', {
        controller: 'ReportedMessageListCtrl',
        templateUrl:'/ryhmasahkoposti-app/reporting/html/reportedMessageList.html'
    }).when('/reportMessages/search', {
        controller: 'ReportedMessageListCtrl',
        templateUrl:'/ryhmasahkoposti-app/reporting/html/reportedMessageList.html'
    }).when('/reportMessages/view/:messageID', {
        controller: 'ReportedMessageViewCtrl',
        templateUrl: '/ryhmasahkoposti-app/reporting/html/reportedMessageView.html'
    }).otherwise({redirectTo:'/reportMessages/list'});
});