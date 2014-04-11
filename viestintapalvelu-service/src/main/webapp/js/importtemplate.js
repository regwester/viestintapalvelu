angular.module('app').controller('TemplateController', ['$scope', '$http', '$window', function ($scope, $http, $window) {
    var templateUrl = "api/v1/template/";

    // get template  
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
		// add template
		url += "templateFile=" + $scope.filenames + "&";
    	
    	// add style
    	if ($scope.styleFile) {
    		url += "styleFile=" + $scope.styleFile + "&"; 
    	}
    	
    	// add language
    	url += "lang=" + $scope.language;
    	
    	// get template
    	getTemplate(url);
    }
	
	// get template
	function getTemplate(url) {
      $http.get(url).
     	success(function (data) {
        	console.dir(data);
        	$scope.templateData=data;
        	$scope.showTemplateData=true;
      	}).
     	error(function (data) {
     		$window.alert("Import epäonnistui");
     	})
	}    
    
	// store function
	$scope.store = function () {
		var url = templateUrl + "store";
		
		console.dir($scope.templateData);
		
    	// store template
    	storeTemplate(url, $scope.templateData);
    }
	
	// store template
	function storeTemplate(url, params) {
	      $http.post(url, params).
	     	success(function (data) {
	        	console.dir(data);
	     		$window.alert("Tallenna on onnistunut");
	     		$scope.templateData=null;
	     	  	$scope.showTemplateData=false;
	     	  	$scope.files=null;
	     	  	$scope.styleFile=null;
	     	  	$scope.language=null;
	     	}).
	     	error(function (data) {
	     		$window.alert("Tallentamiseen epäonnistui");
	     	})
		}    
}]);
