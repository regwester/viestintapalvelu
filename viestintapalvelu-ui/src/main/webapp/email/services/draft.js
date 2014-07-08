'use strict';

angular.module('email')
.factory('DraftService', ['$resource', function($resource) {
  return $resource('/ryhmasahkoposti-service/drafts/:draftId', {draftId: '@id'});
}]);