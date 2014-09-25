'use strict';

angular.module('viestintapalvelu').factory('Global', function() {
  return {
    getUserLanguage: function() {
      var i, lang, max;

      if(window.myroles) {
        for (i = 0, max = window.myroles.length; i < max; i++) {
          if (window.myroles[i].indexOf('LANG_') == 0) {
            lang = window.myroles[i].substring(5);
            if(['fi', 'sv', 'en'].indexOf(lang) != -1) { // returns index or -1 if lang is not in array
              return lang;
            }
          }
        }
      }

      return 'fi'; // Language defaults to finnish
    }
  }
});