'use strict';

angular.module('viestintapalvelu')
.filter('i18n', [
  function() {

    var language = window.navigator.userLanguage || window.navigator.language; //works IE/SAFARI/CHROME/FF
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
      cache: true,            
      language: language,
      callback: function() {
      }
    });

    return function(text) {
    	return jQuery.i18n.prop(text);
    };
  }
]);
