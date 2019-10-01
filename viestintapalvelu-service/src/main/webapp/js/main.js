var app = angular.module('app', ['ngResource', 'ui.tinymce', 'ui.bootstrap', 'ngCookies']);

window.CONFIG = {};
window.CONFIG.env = {};
window.CONFIG.app = {};

app.value("globalConfig", window.CONFIG || {});

app.config(function ($locationProvider) {
    $locationProvider.html5Mode(true).hashPrefix('!');
});

app.config(function($sceProvider) {
	// Control SCE. Do not disable SCE ever in new projects.
	// This project had some problems when migrating from angular 1.0.x -> 1.2
	// and disabling SCE helped during migration. Now enabled again. 
	
	$sceProvider.enabled(true); // 
}).factory('NoCacheInterceptor', function () {
    return {
        request: function (config) {
            if (config.method && config.method == 'GET' && config.url.indexOf('html') === -1){
                var separator = config.url.indexOf('?') === -1 ? '?' : '&';
                config.url = config.url+separator+'noCache=' + new Date().getTime();
            }
            return config;
        }
    };
}).config(['$httpProvider', function($httpProvider) {
    $httpProvider.interceptors.push('NoCacheInterceptor');
}]);

app.run(function($http, $cookies) {
    $http.defaults.headers.common['Caller-Id'] = "viestintapalvelu.viestintapalvelu-service.frontend";
    if($cookies['CSRF']) {
        $http.defaults.headers.common['CSRF'] = $cookies['CSRF'];
    }
})
