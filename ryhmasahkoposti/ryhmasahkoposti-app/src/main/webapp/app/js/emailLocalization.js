'use strict';

angular.module('localization', [])
	.filter('i18n', ['$rootScope', '$locale', '$window', function($rootScope, $locale, $window) {
        var language = window.navigator.userLanguage || window.navigator.language; //works IE/SAFARI/CHROME/FF
//        alert("language: " + language);         
        
        
        if (language) {
            language.substring(0, 2).toUpperCase();

            if (language != "FI" && language != "SV") {
                language = "FI";
            }
        } else {
            language = "FI";
        }

        jQuery.i18n.properties({
            name:'messages',
            path:'./i18n/',
            mode:'map',
            cache: true,            
            language: language,
            callback: function() {
            }
        });

        return function(text) {
        	return jQuery.i18n.prop(text);
        };
	}]);
