'use strict';

var app = angular.module('ryhmasahkopostiApp', ['ryhmasahkopostiSelailuController', 'ryhmasahkopostiTiedotController', 'ryhmasahkopostiFilters']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
        when('/ryhmasahkoposti/selailu', {
            controller: 'RyhmasahkopostiSelailuController',
            templateUrl:'/html/ryhmasahkopostiSeilailu.html'
        }).when('/ryhmasahkoposti/selailu/hae', {
            controller: 'RyhmasahkopostiSelailuController',
            templateUrl:'/html/ryhmasahkopostiSeilailu.html'
        }).when('/ryhmasahkoposti/nayta', {
            controller: 'RyhmasahkopostiTiedotController',
            templateUrl:'/html/ryhmasahkopostiTiedot.html'
        }).otherwise({redirectTo:'/ryhmasahkoposti/selailu'});
}]);

