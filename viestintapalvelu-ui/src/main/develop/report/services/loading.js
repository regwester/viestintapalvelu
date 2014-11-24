'use strict';

angular.module('loading')
.factory('loadingService', function() {
  var service = {
    requestCount: 0,
    isLoading: function() {
      return service.requestCount > 0;
    }
  };
  return service;
})

.factory('onStartInterceptor', ['loadingService', function(loadingService) {
    return function (data, headersGetter) {
        loadingService.requestCount++;
        return data;
    };
}])

.factory('onCompleteInterceptor', ['loadingService', '$q', function(loadingService, $q) {
  return function(promise) {
    var decrementRequestCountSuccess = function(response) {
        loadingService.requestCount--;
        return response;
    };
    var decrementRequestCountError = function(response) {
        loadingService.requestCount--;
        return $q.reject(response);
    };
    return promise.then(decrementRequestCountSuccess, decrementRequestCountError);
  };
}])

.config(['$httpProvider', function($httpProvider) {
    $httpProvider.responseInterceptors.push('onCompleteInterceptor');
}])

.run(['$http', 'onStartInterceptor', function($http, onStartInterceptor) {
    $http.defaults.transformRequest.push(onStartInterceptor);
}])

.controller('LoadingCtrl', ['$scope', '$rootElement', 'loadingService', function($scope, $rootElement, loadingService) {
    $scope.$watch(function() {
        return loadingService.isLoading();
    }, function(value) {
        $scope.loading = value;
        if(value) {
          $rootElement.addClass('spinner');
        } else {
          $rootElement.removeClass('spinner');
        }
    });
}]);