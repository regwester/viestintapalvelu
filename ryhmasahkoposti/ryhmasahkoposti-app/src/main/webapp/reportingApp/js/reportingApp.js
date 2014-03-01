'use strict';

var reportingApp = angular.module('reportingApp', ['ngRoute', 'ngResource']);

reportingApp.config(function ($routeProvider) {
	$routeProvider.
		when('/reportMessages/list', {
            controller: 'ReportedMessageListController',
            resolve: {
                reportedMessages: ['GetReportedMessages', function(GetReportedMessages) {
                  return GetReportedMessages.get();
                }]
            },
            templateUrl:'/ryhmasahkoposti-app/reportingApp/html/reportedMessageList.html'
        }).when('/reportMessages/search/:searchArgument', {
            controller: 'ReportedMessageListController',
            templateUrl:'/ryhmasahkoposti-app/reportingApp/html/reportedMessageList.html'
        }).when('/reportMessages/view/:messageID', {
        	controller: 'ReportedMessageViewController',
        	templateUrl: '/ryhmasahkoposti-app/reportingApp/html/reportedMessageView.html'        	
        }).otherwise({redirectTo:'/reportMessages/list'});
});

