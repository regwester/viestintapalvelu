'use strict';

angular.module('listpaging', []); //no reason for this, but oh well (middle of refactoring)
angular.module('localization', []);
angular.module('loading', []);

var modules = ['ngRoute', 'ngResource', 'ui.bootstrap', 'listpaging', 'localization'];
angular.module('reportingApp', modules);
