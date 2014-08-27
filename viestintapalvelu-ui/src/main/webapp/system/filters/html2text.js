'use strict';

angular.module('viestintapalvelu')
.filter('htmlToPlainText', function() {
  return function(text) {
    //Change html entities to plain text and remove tags
    return String(text).replace(/<[^>]+>/gm, '');
  };
});