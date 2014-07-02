'use strict';

angular.module('localization').
    filter('i18n', ['$rootScope', '$locale', '$window', function($rootScope, $locale, $window) {
        var language = $window.navigator.userLanguage || $window.navigator.language;

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
            path:'../assets/i18n/',
            mode:'map',
            language: language,
            callback: function() {
            }
        });

        return function(text) {
            return jQuery.i18n.prop(text);
        };
    }]);
