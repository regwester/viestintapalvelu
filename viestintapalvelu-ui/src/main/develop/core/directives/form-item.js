'use strict';

angular.module('core.directives')
    .directive('formItem', [function factory() {
        return {
            restrict: 'E',
            transclude: true,
            templateUrl: 'views/core/views/partials/form-item.html',
            scope: {
                'label': '@',
                'required': '='
            }
        };
    }
    ]);