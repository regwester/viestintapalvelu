'use strict';

angular.module('listpaging', []); //no reason for this, but oh well (middle of refactoring)
angular.module('loading', []);

var modules = ['listpaging', 'loading'];
angular.module('report', modules);
