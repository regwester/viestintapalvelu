/* Workaround for source maps in angular exceptions:
* https://github.com/angular/angular.js/issues/5217 */
angular.module('viestintapalvelu')
    .config(['$provide', function($provide) {
        $provide.decorator('$exceptionHandler', ['$delegate', function($delegate) {
            return function (exception, cause) {
                $delegate(exception, cause);
                throw exception;
            }
        }]);
    }]);