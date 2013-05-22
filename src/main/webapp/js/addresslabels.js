angular.module('app').controller('OsoitetarratController', ['$scope', 'Generator', 'Printer' ,function($scope, Generator, Printer) {	
	$scope.addressLabels = [];
	
	function generateLabels(count) {
		$scope.addressLabels = $scope.addressLabels.concat(Generator.generateObjects(count, function(data) {
			var postoffice = data.any('postoffice')
			var country = data.prioritize(['FINLAND', 'FI'], 0.95).otherwise(data.any('country'))
			return {
				"firstName": data.any('firstname'),
				"lastName": data.any('lastname'),
				"addressline": data.any('street') + ' ' + data.any('housenumber'),
				"addressline2": "",
				"addressline3": "",
				"postalCode": postoffice.substring(0, postoffice.indexOf(' ')),
				"city": postoffice.substring(postoffice.indexOf(' ') + 1),
				"region": "",
				"country": country[0],
				"countryCode": country[1]
			}
		}))
	}
	$scope.updateGenerated = function() {
		if ($scope.count > $scope.addressLabels.length) {
			generateLabels($scope.count - $scope.addressLabels.length)
		} else if ($scope.count < $scope.addressLabels.length) {
			$scope.addressLabels.splice($scope.count, $scope.addressLabels.length)
		}
	}
	$scope.removeLabel = function(index) {
		$scope.addressLabels.splice(index, 1)
		$scope.count--;
	}
	$scope.addLabel = function() {
		$scope.addressLabels.unshift({
			"firstName": $scope.firstName,
			"lastName": $scope.lastName,
			"addressline": $scope.addressline,
			"addressline2": $scope.addressline2,
			"addressline3": $scope.addressline3,
			"postalCode": $scope.postalCode,
			"region": $scope.region,
			"city": $scope.city,
			"country": $scope.country
		})
		$scope.count++;
		$scope.firstName = '';
		$scope.lastName = '';
		$scope.addressline = '';
		$scope.addressline2 = '';
		$scope.addressline3 = '';
		$scope.postalCode = '';
		$scope.region = '';
		$scope.city = '';
		$scope.country = '';
	}
	$scope.generatePDF = function() {
		Printer.osoitetarratPDF($scope.addressLabels)
	}
	$scope.generateXLS = function() {
		Printer.osoitetarratXLS($scope.addressLabels)
	}
}]);
