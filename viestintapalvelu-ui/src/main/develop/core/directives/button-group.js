'use strict';

angular.module('core.directives')
    .directive('buttonGroup', [function factory() {
        return {
            restrict: 'E',
            replace: true,
            templateUrl: 'views/partials/button-group.html',
            scope: {
                'commonClass': '@class',
                'buttons': '='
            }
        };
    }
    ]);