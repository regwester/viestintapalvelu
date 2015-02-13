angular.module('core.services').directive('auth', ['$timeout', 'AuthService', function($timeout, AuthService) {
    return {
        link : function($scope, element, attrs) {

            element.addClass('ng-hide');

            var success = function() {
               element.removeClass('ng-hide');
            };
            
            $timeout(function() {
                switch (attrs.auth) {
                
                case "crudOph":
                    AuthService.crudOph().then(success);
                    break;
                case "read":
                    AuthService.read().then(success);
                    break;
            	case "crudOrg":
            	    AuthService.crudDraftOrg(attrs.authOrg).then(success);
                    break;
                }
            }, 0);
            
        }
    };
}]);