'use strict';

angular.module('listpaging').
filter('startFrom',
    function() {
        return function(input, start) {
            return input.slice(start);
        };
    }
);