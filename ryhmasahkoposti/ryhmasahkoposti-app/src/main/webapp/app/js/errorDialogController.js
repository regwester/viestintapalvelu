'use strict';


var viestintapalveluApp = angular.module('viestintapalvelu');

viestintapalveluApp.controller('ErrorDialogController', ['$scope', '$modalInstance', 'msg',
	function ErrorDialogController($scope, $modalInstance, msg) {
	    $scope.msg = (angular.isDefined(msg)) ? msg : 'Tuntematon virhe tapahtunut palvelukerroksessa';
	 
	    $scope.close = function () {
	        $modalInstance.close();
	    };
}]);