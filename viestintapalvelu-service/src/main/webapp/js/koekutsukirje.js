angular.module('app').controller(
		'KoekutsuController', 
		['$scope', 'Generator', 'Printer', function ($scope, Generator, Printer) {
    $scope.koekutsukirjeet = [];
    $scope.count = 0
    $scope.select_min = 0 // Min count of letters selectable from UI drop-down list
    $scope.select_max = 100;  // Max count
    $scope.tinymceModel='<p><i>Tervehdys</i></p><p>Leipäteksti</p><pre>Allekirjoitus</pre>';
    function generateKoekutsukirje(count) {
        $scope.koekutsukirjeet = $scope.koekutsukirjeet.concat(Generator.generateObjects(count, function (data) {
            var postoffice = data.any('postoffice')
            var country = data.prioritize(['FINLAND', 'FI'], 0.95).otherwise(data.any('country'))
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
                "languageCode": data.prioritize('FI', 0.80).prioritize('SE', 0.60).otherwise(data.any('language')),
                "hakukohde" : data.any('hakukohteenNimi'),
                "letterBodyText" : $scope.tinymceModel
            }
        }))
    }

	
    $scope.generatePDF = function () {
    	Printer.koekutsukirjePDF($scope.koekutsukirjeet);
    }
    
    $scope.updateGenerated = function () {
        if ($scope.count > $scope.koekutsukirjeet.length) {
            generateKoekutsukirje($scope.count - $scope.koekutsukirjeet.length)
        } else if ($scope.count < $scope.koekutsukirjeet.length) {
            $scope.koekutsukirjeet.splice($scope.count, $scope.koekutsukirjeet.length)
        }
    }
    $scope.getCount = function () {
    	return $scope.count ? $scope.count : 0
    }

}]);

