'use strict';

angular.module('email')
.factory('DraftService', ['$resource', function($resource) {
  return $resource('/ryhmasahkoposti-service/drafts/:draftId', {draftId: '@id'},
      {
          //Custom resource methods
          count: {
            method: 'GET',
            isArray: false,
            url: '/ryhmasahkoposti-service/drafts/count'
          }
      });
}]);