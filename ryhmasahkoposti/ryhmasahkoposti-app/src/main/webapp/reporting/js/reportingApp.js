'use strict';

var reportingApp = angular.module('reportingApp', ['ngRoute', 'ngResource', 'ui.bootstrap', 'listpaging', 'localization']);

reportingApp.config(function ($routeProvider) {
	$routeProvider.
		when('/reportMessages/list', {
            controller: 'ReportedMessageListController',
            templateUrl:'/ryhmasahkoposti-app/reporting/html/reportedMessageList.html'
        }).when('/reportMessages/search', {
            controller: 'ReportedMessageListController',
            templateUrl:'/ryhmasahkoposti-app/reporting/html/reportedMessageList.html'
        }).when('/reportMessages/view/:messageID', {
        	controller: 'ReportedMessageViewController',
        	templateUrl: '/ryhmasahkoposti-app/reporting/html/reportedMessageView.html'
        }).otherwise({redirectTo:'/reportMessages/list'});
});

