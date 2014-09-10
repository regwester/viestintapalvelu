'use strict';

angular.module('viestintapalvelu')
  .filter('trustAsHtml', ['$sce', function($sce){
    return function(html) {
      return $sce.trustAsHtml(html);
    };
  }]);