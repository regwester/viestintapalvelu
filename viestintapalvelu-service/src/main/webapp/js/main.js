var app = angular.module('app', ['ngResource', 'ui.tinymce', 'ui.bootstrap']);

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