'use strict';

angular.module('core.directives')
    .directive('inputList', [function factory() {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: './core/views/partials/input-list.html',
            scope: {
                'label': '@',
                'items': '=',
                'input': '@',
                'model': '=',
                'change': '='
            }
        };
    }
    ]);