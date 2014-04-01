angular.module('app').controller(
		'LetterController', 
		['$scope', 'Generator', 'Printer', function ($scope, Generator, Printer) {
    $scope.letters = [];
    $scope.count = 0;
    $scope.select_min = 0; // Min count of letters selectable from UI drop-down list
    $scope.select_max = 100;  // Max count
    $scope.tinymceModel='<p>Onneksi olkoon,</p>'
        +'<p>'
        +'    sinut on hyväksytty opiskelemaan yllämainittuun koulutukseen. Tarkistamme valintaan vaikuttaneet'
        +'    koulu- ja työtodistukset sekä muut mahdolliset todistukset. Opiskelupaikkasi voidaan perua jos'
        +'    olet ilmoittanut virheellisiä tietoja.'
        +'</p>'
        +'<p>'
        +'    Ilmoita opiskelupaikan vastaanottamisesta oppilaitokseen viimeistään 29.11.2013. Muuten menetät sinulle'
        +'    varatun opiskelupaikan.'
        +'</p>'

        +'<p>'
        +'    Opiskelijaksi hyväksymisen jälkeen mahdolliset alemmat hakutoiveesi ovat peruuntuneet automaattisesti,'
        +'    etkä voi tulla niihin enää valituksi. Ylempiin hakutoiveisiin voit kuitenkin vielä tulla valituksi'
        +'    varasijalta.'
        +'</p>'

        +'<br/>'
        +'<br/>'

        +'<p>'
        +'    <b>Tervetuloa opiskelemaan!</b>'
        +'</p>'

        +'<br/>'
        +'<br/>';
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
                "templateReplacements": {"tulokset" : tulokset },
                           
                //"letterBodyText" : $scope.tinymceModel
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
	        "selite": data.any('valinnanTulos')
	    };
	});
	}
    $scope.generatePDF = function () {
    	Printer.letterPDF($scope.letters, {"letterBodyText" : $scope.tinymceModel, "hakukohde" : "Tässä lukee hakukohde", "tarjoaja": "Tässä tarjoajan nimi"}, 145);
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

