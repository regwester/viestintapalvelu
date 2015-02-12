'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateIndexCtrl', ['$scope','$state', '$http', '$location', function($scope, $state, $http, $location){
        $http.get("/viestintapalvelu/api/v1/template/ok").success(function (data) {}).error(function(data, status, headers, config) {}).then(function(result) {});
        $scope.isOverviewState = function() {
            return $state.is('letter-templates.overview');
        }
    }]);