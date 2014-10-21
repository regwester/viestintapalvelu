'use strict';

angular.module('localization')
.filter('i18n', ['Global',
  function(Global) {

    var language = Global.getUserLanguage();

    jQuery.i18n.properties({
      name:'messages',
      path:'./assets/i18n/',
      mode:'map',
      cache: true,
      language: language.toUpperCase(),
      callback: function() {
      }
    });

    return function(text) {
      return jQuery.i18n.prop(text);
    };
  }
]);
