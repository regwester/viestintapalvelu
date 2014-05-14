angular.module('app').controller(
		'PrintController', ['$scope', 'Generator', 'Printer', function ($scope, Generator, Printer) {
    $scope.sources = [];
    $scope.filename = "tuloste";
    $scope.content='';

    $scope.generatePDF = function () {
		$scope.sources = [$scope.content];
		Printer.printPDF($scope.sources, $scope.filename);
    };
    
}]);
