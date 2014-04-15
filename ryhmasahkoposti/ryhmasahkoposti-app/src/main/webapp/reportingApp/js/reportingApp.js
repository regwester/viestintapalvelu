'use strict';

var reportingApp = angular.module('reportingApp', ['ngRoute', 'ngResource', 'ui.bootstrap', 'listpaging', 'localization']);

reportingApp.config(function ($routeProvider) {
	$routeProvider.
		when('/reportMessages/list', {
            controller: 'ReportedMessageListController',
            templateUrl:'/ryhmasahkoposti-app/reportingApp/html/reportedMessageList.html'
        }).when('/reportMessages/search', {
            controller: 'ReportedMessageListController',
            templateUrl:'/ryhmasahkoposti-app/reportingApp/html/reportedMessageList.html'
        }).when('/reportMessages/view/:messageID', {
        	controller: 'ReportedMessageViewController',
        	templateUrl: '/ryhmasahkoposti-app/reportingApp/html/reportedMessageView.html'        	
        }).otherwise({redirectTo:'/reportMessages/list'});
});

