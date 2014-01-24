var app = angular.module('app', ['ngResource']);

app.config(function ($locationProvider) {
    $locationProvider.html5Mode(true).hashPrefix('!');
});


angular.module('app').controller('TabsController', ['$scope', '$http', '$compile', function ($scope, $http, $compile) {
    $scope.showContent = function (uri, target) {
        var tabContent = $(target);
        if (tabContent.is(':empty')) {
            $http.get(uri).success(function (html) {
                var scope = angular.element(tabContent).scope();
                tabContent.html($compile(html)(scope));
            })
        }
    }
}])
