"use strict";

var app = angular.module('ajastuspalvelu', [ 'ngResource', 'ngRoute', 'ngAnimate', 'ui.bootstrap']);

app.config([ '$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
    $routeProvider
    .when('/etusivu', {
        controller : 'TaskListController',
        templateUrl : 'partials/frontpage.html'
    })
    .otherwise({
        redirectTo : '/etusivu'
    });
}]);