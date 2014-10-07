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
        
        function getExamples() {
		    return $http.get(template+'getAvailableExamples');
        }

        function getByName(t) {
            var url = template+'getByName?templateName='+t.name+'&languageCode='+t.lang;
            if (t.applicationPeriod) {
                url += "&applicationPeriod="+ t.applicationPeriod;
            }
		    return $http.get(url);
        }

        function listVersionsByName(t, getContents, getPeriods) {
            var url = template+'listVersionsByName?templateName='+t.name+'&languageCode='+t.lang;
            if (getContents) {
                url += "&content=YES";
            }
            if (getPeriods) {
                url += "&periods=YES";
            }
            return $http.get(url);
        }

        function getHistory(t, oid, applicationPeriod, tag){
        	if (tag != null && tag != "") {
        		return $http.get(template+'getHistory?templateName='+t.name+'&languageCode='+t.lang+'&oid='+oid+"&applicationPeriod="+applicationPeriod+"&tag="+tag);
        	} else {
        	 	return $http.get(template+'getHistory?templateName='+t.name+'&languageCode='+t.lang+'&oid='+oid);
        	}
        }

        function saveAttachedApplicationPeriods(templateId, applicationPeriods, useAsDefault) {
            return $http.put(template+"saveAttachedApplicationPeriods", {
                templateId: templateId,
                applicationPeriods: applicationPeriods,
                useAsDefault: useAsDefault
            });
        }

        return {
            getNames: getNames,
            getExamples: getExamples,
            getByName: getByName,
            getHistory: getHistory,
            listVersionsByName: listVersionsByName,
            saveAttachedApplicationPeriods: saveAttachedApplicationPeriods
        };
	}();
}]);

angular.module('app').factory('Printer', ['$http', '$window', function ($http, $window) {
    var addressLabel = 'api/v1/addresslabel/';
    var letter = 'api/v1/letter/';
    var printurl = 'api/v1/printer/';
    var download = 'api/v1/download/';

    return function () {
        function osoitetarratPDF(labels) {
            print(addressLabel + 'pdf', {"addressLabels": labels})
        }

        function osoitetarratXLS(labels) {
            print(addressLabel + 'xls', {"addressLabels": labels})
        }

        function letterPDF(letters, replacements, tName, tLang, oid, applicationPeriod, tag) {
        	print(letter + 'pdf', {
                "letters": letters, "templateReplacements" : replacements, "templateName" : tName, "languageCode" : tLang, "organizationOid" : oid, "applicationPeriod": applicationPeriod, "tag": tag});
        }

        function asyncLetter(letters, replacements, tName, tLang, oid, applicationPeriod, tag) {
            return $http.post(letter+"async/letter", {
                    "letters": letters,
                    "templateReplacements" : replacements,
                    "templateName" : tName,
                    "languageCode" : tLang,
                    "organizationOid" : oid,
                    "applicationPeriod": applicationPeriod,
                    "tag": tag
                }).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Async PDF-kutsu epäonnistui: " + data);
                });
        }

        function asyncStatus(id) {
            return $http.get(letter+"async/letter/status/"+id).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Async status -kutsu epäonnistui: " + data);
                });
        }

        function languageOptions(id) {
            return $http.get(letter+"languageOptions/"+id).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Kielivaihtoehtojen haku epäonnistui: " + data);
                });
        }

        function sendEmail(id) {
            return $http.post(letter+"emailLetterBatch/"+id).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Sähköpostiviestin lähetys kirjelähetykselle "+id+" epäonnistui: " + data);
            });
        }

        function previewEmail(id, langCode) {
            if (langCode) {
                var lc = langCode;
                $http.get(letter+'previewLetterBatchEmail/'+id+"?language="+lc).success(function() {
                    $window.location = letter+'previewLetterBatchEmail/'+id+"?language="+lc;
                }).error(function() {
                    $window.alert("Ei löytynyt kielellä " + lc);
                });
            } else {
                $window.location = letter+'previewLetterBatchEmail/'+id;
            }
        }

        function letterZIP(letters, replacements, tName, tLang, oid, applicationPeriod, tag) {
            print(letter + 'zip', {
                "letters": letters, "templateReplacements" : replacements, "templateName" : tName, "languageCode" : tLang, "organizationOid" : oid, "applicationPeriod": applicationPeriod, "tag": tag});
        }
        
        function ipostZIP(letters) {
            print(jalkiohjauskirje + 'zip', {
                "letters": letters});
        }

        function printPDF(sources, filename) {
            print(printurl + 'pdf', {"documentName": filename, "sources": sources});
        }
        
        function print(url, batch) {
            $http.post(url, batch).
            	success(function (data) {
            		$window.location.href = data;
            	}).
            	error(function (data) {
            		// This is test-ui so we use a popup for failure-indication against guidelines (for production code)
            		$window.alert("Tulostiedoston luonti epäonnistui");
            	});
        }

        return {
            ipostZIP: ipostZIP,
            osoitetarratPDF: osoitetarratPDF,
            osoitetarratXLS: osoitetarratXLS,
            letterPDF: letterPDF,
            letterZIP: letterZIP,
            printPDF: printPDF,
            asyncLetter: asyncLetter,
            asyncStatus: asyncStatus,
            sendEmail: sendEmail,
            previewEmail: previewEmail,
            languageOptions: languageOptions
        }
    }()
}])
