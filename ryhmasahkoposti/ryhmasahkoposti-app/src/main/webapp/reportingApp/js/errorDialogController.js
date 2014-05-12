'use strict';

reportingApp.controller('ErrorDialogController', 
	function ErrorDialogController($scope, $modalInstance, msg) {
	    $scope.msg = (angular.isDefined(msg)) ? msg : 'Tuntematon virhe tapahtunut palvelukerroksessa';
	 
	    $scope.close = function () {
	        $modalInstance.close();
	    };
});