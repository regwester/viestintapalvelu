'use strict';

angular.module('core.filters', []);
angular.module('core.services', []);
angular.module('core.directives', []);
angular.module('core', ['core.filters', 'core.services', 'core.directives']);

angular.module('viestintapalvelu', ['ngResource', 'ui.router', 'ui.bootstrap', 'report','email', 'core', 'template']);
