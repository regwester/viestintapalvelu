angular.module('app').controller('TemplateController', ['$scope', '$http', '$window', function ($scope, $http, $window) {
    var templateUrl = "api/v1/template/";

	$scope.import = function () {
		var url = templateUrl + "get?";
		
		// split files if any
		if ($scope.files != null) 
			$scope.filenames = $scope.files.split(',');
		else {
			alert("Tiedostojen nimet eivät voi olla tyhjiä.");
			return;
		}
		
		// set default if not defined
		if ($scope.language == null)
			$scope.language = "FI";
			
		// create url
    	for (var int = 0; int < $scope.filenames.length; int++) {
    		var filename = $scope.filenames[int];
    		url += "templateFile=" + filename + "&";
    	}
    	url += "lang=" + $scope.language;
    	
    	// get template
    	getTemplate(url);
    }
	
	// get template
	function getTemplate(url) {
      $http.get(url).
     	success(function (data) {
        	console.dir(data);
        	
        	// store template
        	var url = templateUrl + "store";
        	storeTemplate(url, data);
     	}).
     	error(function (data) {
     		// This is test-ui so we use a popup for failure-indication against guidelines (for production code)
     		$window.alert("Import epäonnistui");
     	})
	}    
    
	// store template
	function storeTemplate(url, params) {
	      $http.post(url, params).
	     	success(function (data) {
	        	console.dir(data);
	     	}).
	     	error(function (data) {
	     		// This is test-ui so we use a popup for failure-indication against guidelines (for production code)
	     		$window.alert("Tallentamiseen epäonnistui");
	     	})
		}    
}]);
