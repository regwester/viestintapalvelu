var app = angular.module('app', ['ngResource']);

angular.module('app').controller('TabsController', ['$scope', '$http', '$compile', function($scope, $http, $compile) {	
	$scope.showContent = function(uri, id) {
		var tabContent = $(id);
		if (tabContent.is(':empty')) {
	        $http.get(uri).success(function(html) {
	        	var scope = angular.element(tabContent).scope();
	        	tabContent.html($compile(html)(scope));
	        })
		}
	}
}])
