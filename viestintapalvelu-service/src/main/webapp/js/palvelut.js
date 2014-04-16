angular.module('app').factory('Generator', ['Firstnames', 'Lastnames', 'Streets', 'Postoffices', 'Countries', 'Language', 'Koulu', 'Hakutoive', function (Firstnames, Lastnames, Streets, Postoffices, Countries, Language, Koulu, Hakutoive) {
    var generatedData = {}
    generatedData['housenumber'] = _.range(1, 200)
    Firstnames.success(function (data) {
        generatedData['firstname'] = data
    })
    Lastnames.success(function (data) {
        generatedData['lastname'] = data
    })
    Streets.success(function (data) {
        generatedData['street'] = data
    })
    Postoffices.success(function (data) {
        generatedData['postoffice'] = data
    })
    Countries.success(function (data) {
        generatedData['country'] = data
    })
    Language.success(function (data) {
        generatedData['language'] = data
    })
    Koulu.success(function (data) {
        generatedData['organisaationNimi'] = data
    })
    Hakutoive.success(function (data) {
        generatedData['hakukohteenNimi'] = data
    })
    generatedData['oppilaitoksenNimi'] = ['lukio', 'ammattikoulu', 'ammattioppilaitos', 'avoin yliopisto', 'korkeakoulu']
    generatedData['hakutoive-lukumaara'] = _.range(1, 5)
    generatedData['hyvaksytyt'] = _.range(20, 100)
    generatedData['kaikkiHakeneet'] = _.range(100, 200)
    generatedData['paikat'] = _.range(20, 50)
    generatedData['varasija'] = _.range(1, 10)
    generatedData['alinHyvaksyttyPistemaara'] = _.range(40, 50)
    generatedData['pisteetvajaa'] = _.range(10, 39)
    generatedData['koe'] = _.range(0, 20)
    generatedData['valinnanTulos'] = ['Hylätty', 'Varasijalla', 'Hyväksytty']

    return function () {
        function any(dataid, index) {
            var data = generatedData[dataid];
            var item = data[Math.round(Math.random() * (data.length - 1))];
            if (item instanceof Array) {
                return item;
            }
            return item.toString()
        }

        function prioritize(value, p) {
            var randomValue = null;

            function otherwise(otherValue) {
                return randomValue != null ? randomValue : otherValue;
            }

            function constant() {
                return {
                    otherwise: otherwise,
                    prioritize: constant
                }
            }

            randomValue = Math.random() <= p ? value : null;
            return {
                otherwise: otherwise,
                prioritize: randomValue != null ? constant : prioritize
            }
        }

        function generateObjects(count, createObject) {
            return _.map(_.range(count), function () {
                return createObject({any: any, prioritize: prioritize})
            })
        }

        return {
            generateObjects: generateObjects
        }
    }()
}])

angular.module('app').factory('Firstnames', ['$http', function ($http) {
    return $http.get('generator/firstnames.json')
}])

angular.module('app').factory('Lastnames', ['$http', function ($http) {
    return $http.get('generator/lastnames.json')
}])

angular.module('app').factory('Streets', ['$http', function ($http) {
    return $http.get('generator/streets.json')
}])

angular.module('app').factory('Postoffices', ['$http', function ($http) {
    return $http.get('generator/postoffices.json')
}])

angular.module('app').factory('Countries', ['$http', function ($http) {
    return $http.get('generator/countries.json')
}])

angular.module('app').factory('Language', ['$http', function ($http) {
    return $http.get('generator/language.json')
}])

angular.module('app').factory('Koulu', ['$http', function ($http) {
    return $http.get('generator/koulut.json')
}])

angular.module('app').factory('Hakutoive', ['$http', function ($http) {
    return $http.get('generator/hakutoive.json')
}])

angular.module('app').factory('Template', ['$http', '$window', function ($http, $window) {
	var template = 'api/v1/template/';
	
	return function () {
        function getNames() {
		    return $http.get(template+'getNames');
        }
        
        function getByName(t) {
		    return $http.get(template+'getByName?templateName='+t.name+'&languageCode='+t.lang);
        }
        
        return {
            getNames: getNames,
            getByName: getByName
        }
	}()
}]);


angular.module('app').factory('Printer', ['$http', '$window', function ($http, $window) {
    var addressLabel = 'api/v1/addresslabel/';
    var jalkiohjauskirje = 'api/v1/jalkiohjauskirje/';
    var hyvaksymiskirje = 'api/v1/hyvaksymiskirje/';
    var koekutsukirje = 'api/v1/koekutsukirje/';
    var letter = 'api/v1/letter/';
    var download = 'api/v1/download/';

    return function () {
        function osoitetarratPDF(labels) {
            print(addressLabel + 'pdf', {"addressLabels": labels})
        }

        function osoitetarratXLS(labels) {
            print(addressLabel + 'xls', {"addressLabels": labels})
        }

        function jalkiohjauskirjePDF(letters) {
            print(jalkiohjauskirje + 'pdf', {
                "letters": letters});
        }

        function hyvaksymiskirjePDF(letters) {
            print(hyvaksymiskirje + 'pdf', {
                "letters": letters});
        }

        function koekutsukirjePDF(letters) {
            print(koekutsukirje + 'pdf', {
                "letters": letters});
        }

        function letterPDF(letters, replacements, tName, tLang) {
            print(letter + 'pdf', {
                "letters": letters, "templateReplacements" : replacements, "templateName" : tName, "languageCode" : tLang});
        }
        
        function letterZIP(letters, replacements, tName, tLang) {
            print(letter + 'zip', {
                "letters": letters, "templateReplacements" : replacements, "templateName" : tName, "languageCode" : tLang});
        }
        
        function ipostZIP(letters) {
            print(jalkiohjauskirje + 'zip', {
                "letters": letters});
        }

        function print(url, batch) {
            $http.post(url, batch).
            	success(function (data) {
            		$window.location.href = data;
            	}).
            	error(function (data) {
            		// This is test-ui so we use a popup for failure-indication against guidelines (for production code)
            		$window.alert("Tulostiedoston luonti epäonnistui");
            	})
        }

        return {
            ipostZIP: ipostZIP,
            jalkiohjauskirjePDF: jalkiohjauskirjePDF,
            hyvaksymiskirjePDF: hyvaksymiskirjePDF,
            koekutsukirjePDF: koekutsukirjePDF,
            osoitetarratPDF: osoitetarratPDF,
            osoitetarratXLS: osoitetarratXLS,
            letterPDF: letterPDF,
            letterZIP: letterZIP
        }
    }()
}])
