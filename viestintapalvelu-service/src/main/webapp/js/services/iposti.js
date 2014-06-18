'use strict';

angular.module('app')
.factory('IPosti',['$resource', '$window', '$location',
  function($resource, $window, $location){
	
    return {
    	'unSentItems': $resource('/viestintapalvelu/api/v1/iposti/unSentItems'),
     	'send': $resource('/viestintapalvelu/api/v1/iposti/send/:id', {id: '@id'}),
     	'getById': function(id) {
     		$window.location.href = $location.url() + '/api/v1/iposti/getById/' + id;
     	},
     	'getDetailsById': $resource('/viestintapalvelu/api/v1/letter/getById', {letterBatchId: '@id'}, {query: {method: 'GET', isArray: false}})
    };
    
  }
]);