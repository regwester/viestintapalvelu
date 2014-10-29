'use strict';

angular.module('app').factory('Generator', ['$http', '_',
    function ($http, _) {
        var generatedData = {};
        generatedData.housenumber = _.range(1, 200)

        $http.get('generator/firstnames.json').success(function (data) {
            generatedData.firstname = data;
        });
        $http.get('generator/lastnames.json').success(function (data) {
            generatedData.lastname = data;
        });
        $http.get('generator/streets.json').success(function (data) {
            generatedData.street = data;
        });
        $http.get('generator/postoffices.json').success(function (data) {
            generatedData.postoffice = data;
        });
        $http.get('generator/countries.json').success(function (data) {
            generatedData.country = data;
        });
        $http.get('generator/language.json').success(function (data) {
            generatedData.language = data;
        });
        $http.get('generator/koulut.json').success(function (data) {
            generatedData.organisaationNimi = data;
        });
        $http.get('generator/hakutoive.json').success(function (data) {
            generatedData.hakukohteenNimi = data;
        });

        generatedData.oppilaitoksenNimi = ['lukio', 'ammattikoulu', 'ammattioppilaitos', 'avoin yliopisto', 'korkeakoulu'];
        generatedData.selectionCriteria = ['Yhteispisteet', 'Koepisteet', 'Lähtöpisteet', 'Koulumenestys ja valintakoe', 'Valintakoe'];
        generatedData.hakutoiveLukumaara = _.range(1, 5);
        generatedData.hyvaksytyt = _.range(20, 100);
        generatedData.kaikkiHakeneet = _.range(100, 200);
        generatedData.paikat = _.range(20, 50);
        generatedData.varasija = _.range(1, 10);
        generatedData.alinHyvaksyttyPistemaara = _.range(40, 50);
        generatedData.pisteetvajaa = _.range(10, 39);
        generatedData.koe = _.range(0, 20);

        generatedData.sijoitukset = [];
        generatedData.pisteet = [];

        generatedData.valinnanTulos = ['Hylätty', 'Varasijalla', 'Hyväksytty'];

        return function () {
            function any(propertyName) {
                var data = generatedData[propertyName];
                var item = data[Math.round(Math.random() * (data.length - 1))];
                if (item instanceof Array) {
                    return item;
                }
                return item.toString()
            }

            /**
             * Returns a given amount of random items from a list by first shuffling and then slicing it.
             * If the given amount exceeds or equals the length of the array, all items are returned.
             */
            function randomItems(propertyName, amount) {
                var data = generatedData[propertyName];
                if (data instanceof Array) {
                    return _.shuffle(data).slice(0, amount);
                }
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
                    return createObject({any: any, prioritize: prioritize, randomItems: randomItems})
                })
            }

            return {
                generateObjects: generateObjects
            }
        }()
    }]);