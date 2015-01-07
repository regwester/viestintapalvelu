'use strict';

angular.module('core.services')
    .factory('_', ['$window', function ($window) {
        return $window._;
    }]);