'use strict';

angular.module('localization', []).filter('i18n', [ '$rootScope', '$locale', function($rootScope, $locale) {
    var localeMapping = {
        "en-us" : "EN",
        "fi-fi" : "FI",
        "sv-se" : "SV"
    };
    
    var lang = (!$locale.id) ? $locale.id : "fi-fi"
    
    jQuery.i18n.properties({
        name : 'messages',
        path : './assets/i18n/',
        mode : 'map',
        language : localeMapping[lang],
        callback : function() {
        }
    });

    return function(text) {
        return jQuery.i18n.prop(text);
    };
} ]);