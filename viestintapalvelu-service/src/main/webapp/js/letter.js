angular.module('app').controller(
		'LetterController', 
		['$scope', 'Generator', 'Printer', 'Template', function ($scope, Generator, Printer, Template) {
    $scope.letters = [];
    $scope.count = 0;
    $scope.select_min = 0; // Min count of letters selectable from UI drop-down list
    $scope.select_max = 100;  // Max count
    Template.getNames().success(function (data) {
    	$scope.templates = data;
    });
    $scope.replacements = [];
    $scope.historyList=[];
    $scope.oid = "1.2.246.562.10.00000000001";
    $scope.applicationPeriod = "K2014";
    
    $scope.templateChanged = function() {
    	Template.getByName($scope.template).success(function (data) {
    		$scope.replacements = data;
    		for (i in data.replacements) {
    			var r = data.replacements[i];
    			if (r.name == "sisalto") {
    				$scope.tinymceModel = r.defaultValue;
    			}
    		}
    		
    	});
    	Template.getHistory($scope.template, $scope.oid, $scope.applicationPeriod, $scope.tag).success(function (data) {
    		$scope.historyList = data;
    	});
    };
    
    $scope.historyChanged = function() {
    	for (i in $scope.history.templateReplacements) {
			var r = $scope.history.templateReplacements[i];
			if (r.name == "sisalto") {
				$scope.tinymceModel = r.defaultValue;
			}
    	}
    };
    
    $scope.tinymceModel='';
    function generateLetters(count) {
        $scope.letters = $scope.letters.concat(Generator.generateObjects(count, function (data) {
        var tulokset = generateTulokset(data.any('hakutoive-lukumaara'));
        var postoffice = data.any('postoffice');
        var country = data.prioritize(['FINLAND', 'FI'], 0.95).otherwise(data.any('country'));
            
            return {
                "addressLabel": {
                    "firstName": data.any('firstname'),
                    "lastName": data.any('lastname'),
                    "addressline": data.any('street') + ' ' + data.any('housenumber'),
                    "addressline2": "",
                    "addressline3": "",
                    "postalCode": postoffice.substring(0, postoffice.indexOf(' ')),
                    "region": "",
                    "city": postoffice.substring(postoffice.indexOf(' ') + 1),
                    "country": country[0],
                    "countryCode": country[1]
                },
                "templateReplacements": {"tulokset" : tulokset,
                "koulu": tulokset[0]['organisaationNimi'],
                "koulutus": tulokset[0]['hakukohteenNimi']
                },
                           
            };
        }));
    }

	function generateTulokset(count) {
	return Generator.generateObjects(count, function (data) {
	    return {
	        "organisaationNimi": data.any('organisaationNimi'),
	        "oppilaitoksenNimi": data.any('oppilaitoksenNimi'),
	        "hakukohteenNimi": data.any('hakukohteenNimi'),
	        "hyvaksytyt": data.any('hyvaksytyt'),
	        "kaikkiHakeneet": data.any('kaikkiHakeneet'),
	        "alinHyvaksyttyPistemaara": data.any('alinHyvaksyttyPistemaara'),
	        "omatPisteet": data.any('pisteetvajaa'),
	        "paasyJaSoveltuvuuskoe": data.any('koe'),
	        "valinnanTulos": data.any('valinnanTulos'),
	        
	        "varasija": data.any('varasija'),	        
//	        "selite": data.any('valinnanTulos')
	        "hylkaysperuste": data.any('valinnanTulos')
	        
	    };
	});
	}
    $scope.generatePDF = function () {
    	Printer.letterPDF($scope.letters, {"sisalto" : $scope.tinymceModel, "hakukohde" : "Tässä lukee hakukohde", "tarjoaja": "Tässä tarjoajan nimi"}, $scope.template.name, $scope.template.lang, $scope.oid, $scope.applicationPeriod, $scope.tag);
    };
    
    $scope.generateZIP = function () {
    	Printer.letterZIP($scope.letters, {"sisalto" : $scope.tinymceModel, "hakukohde" : "Tässä lukee hakukohde", "tarjoaja": "Tässä tarjoajan nimi"}, $scope.template.name, $scope.template.lang, $scope.oid, $scope.applicationPeriod, $scope.tag);
    };
    
    $scope.updateGenerated = function () {
        if ($scope.count > $scope.letters.length) {
            generateLetters($scope.count - $scope.letters.length);
        } else if ($scope.count < $scope.letters.length) {
            $scope.letters.splice($scope.count, $scope.letters.length);
        }
    };
    $scope.getCount = function () {
    	return $scope.count ? $scope.count : 0;
    };

}]);

