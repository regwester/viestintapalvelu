'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateIndexCtrl', ['$scope','$state', function($scope, $state){
        $scope.isOverviewState = function() {
            return $state.is('letter-templates.overview');
        }
    }]);