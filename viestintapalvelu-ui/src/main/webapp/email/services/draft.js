'use strict';

angular.module('email')
  .factory('DraftService', ['$resource', function($resource) {

    var selectedDraft;

    return {
      drafts: $resource('/ryhmasahkoposti-service/drafts/:draftId/:id', {draftId: '@id'},
        {
          //Custom resource methods
          count: {
            method: 'GET',
            isArray: false,
            url: '/ryhmasahkoposti-service/drafts/count'
          },
          update: {
            method: "PUT"
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