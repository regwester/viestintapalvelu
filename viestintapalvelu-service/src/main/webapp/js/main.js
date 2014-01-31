var app = angular.module('app', ['ngResource', 'RichTextArea']);

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
});


app.controller('TabsController', ['$scope', '$http', '$compile', function ($scope, $http, $compile) {
    $scope.showContent = function (tabName) {
    	var uri = 'html/' + tabName + '.html'; 
        var tabContent = $("#" + tabName);
        if (tabContent.is(':empty')) {
            $http.get(uri).success(function (html) {
                var tabPageScope = angular.element(tabContent).scope();
                tabContent.html($compile(html)(tabPageScope));
            })
        }
    }
}])