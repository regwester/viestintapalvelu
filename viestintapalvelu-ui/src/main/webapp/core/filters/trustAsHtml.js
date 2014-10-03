'use strict';

angular.module('core.filters')
  .filter('trustAsHtml', ['$sce', function($sce){
    return function(html) {
      return $sce.trustAsHtml(html);
    };
  }]);