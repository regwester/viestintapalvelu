'use strict';

angular.module('email')
  .factory('DraftService', ['$resource', function($resource) {

    var selectedDraft;

    return {
      drafts: $resource('/ryhmasahkoposti-service/drafts/:draftId', {draftId: '@id'},
        {
          //Custom resource methods
          count: {
            method: 'GET',
            isArray: false,
            url: '/ryhmasahkoposti-service/drafts/count'
          }
        }),
      selectDraft: function(id) {
        selectedDraft = id;
      },
      selectedDraft: function() {
        return selectedDraft;
      }
    }
  }]);