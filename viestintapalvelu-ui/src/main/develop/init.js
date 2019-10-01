'use strict';

var app = angular.module('viestintapalvelu', ['ngResource', 'ngCookies', 'ui.router', 'ui.bootstrap', 'report','email', 'core', 'letter-templates']);

app.run(function($http, $cookies) {
    $http.defaults.headers.common['Caller-Id'] = "viestintapalvelu.viestintapalvelu-ui.frontend";
    if($cookies['CSRF']) {
        $http.defaults.headers.common['CSRF'] = $cookies['CSRF'];
    }
})
