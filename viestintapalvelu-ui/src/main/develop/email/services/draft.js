'use strict';

angular.module('email')
  .factory('DraftService', ['$resource', function($resource) {

    var selectedDraft;

    return {
      drafts: $resource(window.url("ryhmasahkoposti-service.draft.resource"), {draftId: '@id'},
        {
          //Custom resource methods
          count: {
            method: 'GET',
            isArray: false,
            url: window.url("ryhmasahkoposti-service.draft.count")
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