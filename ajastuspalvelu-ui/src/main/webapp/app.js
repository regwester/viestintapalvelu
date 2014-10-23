"use strict";

var app = angular.module('ajastuspalvelu', [ 'ngResource', 'ngRoute', 'ngAnimate', 'ui.bootstrap']);

app.config([ '$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/etusivu', {
        controller : 'TaskListController',
        templateUrl : 'partials/frontpage.html'
    })
    .otherwise({
        redirectTo : '/etusivu'
    });
}]);

