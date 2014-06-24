'use strict';

angular.module('app')
.factory('IPosti',['$resource', '$window', '$location',
  function($resource, $window, $location){

    return {
      'unSentItems': $resource('/viestintapalvelu/api/v1/iposti/unSentItems'),
      'sendBatch': $resource('/viestintapalvelu/api/v1/iposti/sendBatch/:id', {id: '@id'}),
      'sendMail': $resource('/viestintapalvelu/api/v1/iposti/sendMail/:id', {id: '@id'}),
      'getBatchById': function(id) {
        $window.location.href = $location.url() + '/api/v1/iposti/getBatchById/' + id;
      },
      'getIPostiById': function(id) {
        $window.location.href = $location.url() + '/api/v1/iposti/getIPostiById/' + id;
      },
      'getDetailsById': $resource('/viestintapalvelu/api/v1/letter/getById', {letterBatchId: '@id'}, {query: {method: 'GET', isArray: false}})
    };

  }
]);