'use strict';

angular.module('core.directives')
    .directive('buttonGroup', [function factory() {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'views/core/views/partials/button-group.html',
            scope: {
                'buttonClass': '@',
                'buttons': '='
            }
        };
    }
    ]);