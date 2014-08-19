'use strict';

angular.module('viestintapalvelu').factory('Global', function() {
  return {
    getUserLanguage: function() {
      var lang = window.navigator.language || window.navigator.userLanguage;
      if (lang) {
        lang = lang.substr(0, 2).toLowerCase();
        if(['fi', 'sv', 'en'].indexOf(lang) != -1) { //returns index or -1 if lang is not in array
          return lang;
        }
      }
      return 'fi'; //Language defaults to finnish
    }
  }
});