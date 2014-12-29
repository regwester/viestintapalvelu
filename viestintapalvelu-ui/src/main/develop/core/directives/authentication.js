angular.module('core.services').directive('auth', ['$timeout', 'AuthService', function($timeout, AuthService) {
    return {
        link : function($scope, element, attrs) {

            element.addClass('ng-hide');

            var success = function() {
                if (additionalCheck()) {
                    element.removeClass('ng-hide');
                }
            };
            var additionalCheck = function() {
                if (attrs.authAdditionalCheck) {
                    var temp = $scope.$eval(attrs.authAdditionalCheck);
                    return temp;
                }
                return true;
            };
            $timeout(function() {
                switch (attrs.auth) {

                case "crudOph":
                    AuthService.crudOph().then(success);
                    break;

                case "readOph":
                    AuthService.readOph().then(success);
                    break;
                }
            }, 0);

            attrs.$observe('authOrg', function() {
                if (attrs.authOrg) {
                    switch (attrs.auth) {
                    case "crud":
                        AuthService.crudDraftOrg(attrs.authOrg).then(success);
                        break;

                    case "read":
                        AuthService.readOrg(attrs.authOrg).then(success);
                        break;
                    }
                }
            });

        }
    };
}]);