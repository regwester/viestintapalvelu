angular.module('app').controller('OsoitetarratController', ['$scope', 'Generator', 'Printer' ,function($scope, Generator, Printer) {	
	$scope.addressLabels = [];
	
	function generateLabels(count) {
		$scope.addressLabels = $scope.addressLabels.concat(Generator.generateObjects(count, function(data) {
			var postoffice = data.any('postoffice')
			return {
				"firstName": data.any('firstname'),
				"lastName": data.any('lastname'),
				"addressline": data.any('street') + ' ' + data.any('housenumber'),
				"addressline2": "",
				"addressline3": "",
				"postalCode": postoffice.substring(0, postoffice.indexOf(' ')),
				"city": postoffice.substring(postoffice.indexOf(' ') + 1),
				"region": "",
				"country": data.prioritize('Finland', 0.95).otherwise(data.any('country'))
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
		function any(dataid) {
			var data = generatedData[dataid]
			return data[Math.round(Math.random()*(data.length-1))].toString()
		}

		function prioritize(value, p) {
			function otherwise(otherValue) {
				return Math.random() <= p ? value : otherValue;
			}
			return {otherwise: otherwise}
		}
		
		function generateObjects(count, createObject) {
			return _.map(_.range(count), function() {
				return createObject({any: any, prioritize: prioritize})
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
