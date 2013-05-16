angular.module('app').factory('Generator', ['Firstnames', 'Lastnames', 'Streets', 'Postoffices', 'Countries', 'Koulu', 'Hakutoive', function(Firstnames, Lastnames, Streets, Postoffices, Countries, Koulu, Hakutoive) {
	var generatedData = {}
	generatedData['housenumber'] = _.range(1, 200)
	Firstnames.success(function(data) {generatedData['firstname'] = data})
	Lastnames.success(function(data) {generatedData['lastname'] = data})
	Streets.success(function(data) {generatedData['street'] = data})
	Postoffices.success(function(data) {generatedData['postoffice'] = data})
	Countries.success(function(data) {generatedData['country'] = data})
	Koulu.success(function(data) {generatedData['koulu'] = data})
	Hakutoive.success(function(data) {generatedData['hakutoive'] = data})
	generatedData['hakutoive-lukumaara'] = _.range(1, 5)
	generatedData['ensisijaiset'] = _.range(20, 100)
	generatedData['hakijat'] = _.range(100, 200)
	generatedData['paikat'] = _.range(20, 50)
	generatedData['varasija'] = _.range(1, 10)
	generatedData['raja'] = _.range(40, 50)
	generatedData['pisteetvajaa'] = _.range(10, 39)
	generatedData['koe'] = _.range(0, 20)
	generatedData['syy'] = ['B', 'C', 'E', 'H', 'K', 'L', 'M', 'S', 'T', 'Y', 'Z']

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

angular.module('app').factory('Koulu', ['$http', function($http){
	return $http.get('generator/koulut.json')
}])

angular.module('app').factory('Hakutoive', ['$http', function($http){
	return $http.get('generator/hakutoive.json')
}])

angular.module('app').factory('Printer', ['$http', '$window', function($http, $window){
	var addressLabel = 'api/v1/addresslabel/';
	var jalkiohjauskirje = 'api/v1/jalkiohjauskirje/';
	var download = 'api/v1/download/document/';

	return function() {
		function osoitetarratPDF(labels) {
			print(addressLabel + 'pdf', {"templateName": "/osoitetarrat.html", "addressLabels": labels})
		}

		function osoitetarratXLS(labels) {
			print(addressLabel + 'xls', {"templateName": "/osoitetarrat.xls", "addressLabels": labels})
		}
		
		function jalkiohjauskirjePDF(letters) {
			print(jalkiohjauskirje + 'pdf', {
				"kirjeTemplateName": "/jalkiohjauskirje.html", 
				"liiteTemplateName": "/liite.html", 
				"letters": letters});
		}
		
		function jalkiohjauskirjePDFDOCX(letters) {
			print(jalkiohjauskirje + 'pdf', {
				"kirjeTemplateName": "/jalkiohjauskirje.docx", 
				"liiteTemplateName": "/liite.docx", 
				"letters": letters});
		}
		
		function print(url, batch) {
	        $http.post(url, batch).success(function(data) {
	        	$window.location.href = download + data;
	        })
		}
		
		return {
			jalkiohjauskirjePDF: jalkiohjauskirjePDF,
			jalkiohjauskirjePDFDOCX: jalkiohjauskirjePDFDOCX,
			osoitetarratPDF: osoitetarratPDF,
			osoitetarratXLS: osoitetarratXLS
		}
	}()
}])
