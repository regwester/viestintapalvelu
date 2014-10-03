'use strict';

angular.module('core.filters')
.filter('htmlToPlainText', function() {
  return function(text) {
    //Change html entities to plain text and remove tags
    return String(text).replace(/<[^>]+>/gm, '');
  };
});