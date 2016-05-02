'use strict';

angular.module('app')
.factory('IPosti',['$resource', '$window', '$location',
  function($resource, $window, $location){

    return {
      'unSentItems': $resource(window.url("viestintapalvelu.iposti.unSentItems")),
      'sendBatch': $resource(window.url("viestintapalvelu.iposti.sendBatch"), {id: '@id'}),
      'sendMail': $resource(window.url('viestintapalvelu.iposti.sendMail'), {id: '@id'}),
      'getBatchById': function(id) {
        $window.location.href = window.url('viestintapalvelu.iposti.getBatchById', id);
      },
      'getIPostiById': function(id) {
        $window.location.href = window.url('viestintapalvelu.iposti.getIPostiById', id);
      },
      'getDetailsById': $resource(window.url("viestintapalvelu.letter.getById"), {letterBatchId: '@id'}, {query: {method: 'GET', isArray: false}})
    };

  }
]);