var app = angular.module('app', ['ngResource']);

angular.module('app').controller('OsoitetarratController', ['$scope', 'Generator', 'Printer' ,function($scope, Generator, Printer) {	
	$scope.addressLabels = [];
	
	function generateLabels(count) {
		$scope.addressLabels = $scope.addressLabels.concat(Generator.generateObjects(count, function(random) {
			var postoffice = random('postoffice')
			return {
				"firstName": random('firstname'),
				"lastName": random('lastname'),
				"streetAddress": random('street') + ' ' + random('housenumber'),
				"postalCode": postoffice.substring(0, postoffice.indexOf(' ')),
				"postOffice": postoffice.substring(postoffice.indexOf(' ') + 1),
				"country": random('country')
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
			"streetAddress": $scope.streetAddress,
			"postalCode": $scope.postalCode,
			"postOffice": $scope.postOffice,
			"country": $scope.country
		})
		$scope.count++;
		$scope.firstName = '';
		$scope.lastName = '';
		$scope.streetAddress = '';
		$scope.postalCode = '';
		$scope.postOffice = '';
		$scope.country = '';
	}
	$scope.generatePDF = function() {
		Printer.pdf($scope.addressLabels)
	}
	$scope.generateCSV = function() {
		Printer.csv($scope.addressLabels)
	}
}]);

angular.module('app').factory('Generator', ['Firstnames', 'Lastnames', 'Streets', 'Postoffices', 'Countries', function(Firstnames, Lastnames, Streets, Postoffices, Countries) {
	var generatedData = {}
	generatedData['housenumber'] = _.range(1, 200)
	Firstnames.success(function(data) {generatedData['firstname'] = data})
	Lastnames.success(function(data) {generatedData['lastname'] = data})
	Streets.success(function(data) {generatedData['street'] = data})
	Postoffices.success(function(data) {generatedData['postoffice'] = data})
	Countries.success(function(data) {generatedData['country'] = data})

	return function() {
		function random(dataid) {
			var data = generatedData[dataid]
			return data[Math.round(Math.random()*(data.length-1))].toString()
		}

		function generateObjects(count, createObject) {
			return _.map(_.range(count), function() {
				return createObject(random)
			})
		}
		
		return {
			generateObjects: generateObjects
		}
	}()
}])

angular.module('app').factory('Firstnames', ['$http', function($http){
	return $http.get('generator/firstnames.json')
}])

angular.module('app').factory('Lastnames', ['$http', function($http){
	return $http.get('generator/lastnames.json')
}])

angular.module('app').factory('Streets', ['$http', function($http){
	return $http.get('generator/streets.json')
}])

angular.module('app').factory('Postoffices', ['$http', function($http){
	return $http.get('generator/postoffices.json')
}])

angular.module('app').factory('Countries', ['$http', function($http){
	return $http.get('generator/countries.json')
}])

angular.module('app').factory('Printer', ['$http', '$window', function($http, $window){
	var createDocument = '/api/v1/addresslabel/createDocument';
	var download = '/api/v1/addresslabel/download/';

	return function() {
		function pdf(labels) {
			print({"templateName": "/osoitetarrat.html", "addressLabels": labels})
		}

		function csv(labels) {
			print({"templateName": "/osoitetarrat.csv", "addressLabels": labels})
		}
		
		function print(batch) {
	        $http.post(createDocument, batch).success(function(data) {
	            $window.open(download + data);
	        })
		}
		
		return {
			pdf: pdf,
			csv: csv
		}
	}()
}])
