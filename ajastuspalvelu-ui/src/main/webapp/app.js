"use strict";

var SERVICE_REST_PATH = "../ajastuspalvelu-service/"

var app = angular.module('ajastuspalvelu', [ 'ngResource', 'ngRoute', 'ngAnimate', 'ui.bootstrap']);

app.config([ '$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/etusivu', {
        controller : 'TaskListController',
        templateUrl : 'partials/frontpage.html'
    })
    .when('/create', {
	controller : 'CreateTaskController',
	templateUrl : 'partials/createtask.html'
    })
    .otherwise({
        redirectTo : '/etusivu'
    });
}]);

