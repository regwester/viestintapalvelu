'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateIndexCtrl', ['$scope','$state', '$http', function($scope, $state, $http){
        $http.get("/viestintapalvelu/api/v1/template/ok").success(function (data) {}).error(function(data, status, headers, config) {});
        $scope.isOverviewState = function() {
            return $state.is('letter-templates.overview');
        }
    }]);