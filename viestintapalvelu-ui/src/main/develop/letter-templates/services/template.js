'use strict';

angular.module('letter-templates')
    .factory('TemplateService', ['$resource', '$http','$q', function ($resource, $http, $q) {

        var serviceUrl = '/viestintapalvelu/api/v1/',
            templateBaseUrl = serviceUrl + 'template/',
            selectedApplicationTarget,
            deferred = $q.defer();

        $http.get(serviceUrl + 'options/hakus').success(function(data) {
            deferred.resolve(data);
        });

        return {
            getHakus: function() {
                return $http.get(serviceUrl + 'options/hakus');
            },

            getByApplicationPeriod: function (applicationPeriod) {
                return $http.get(templateBaseUrl + 'listByApplicationPeriod/' + applicationPeriod);
            },
            getApplicationTargets: function() {
                return deferred.promise;
            },
            getApplicationTarget: function() {
                return selectedApplicationTarget;
            },
            setApplicationTarget: function(value) {
                selectedApplicationTarget = value;
            },
            saveTemplate: function(template) {
                return $http.post(templateBaseUrl + 'insert/', template);
            },
            publishTemplate : function() {
                return true;
            },
            getParsedTreeGrid : function(response) {
                console.log("getParsedTreeGrid");
                console.log(response);
                var map = function(fn, arr) {
                    var result = [];

                    arr.forEach(function(element) {
                        result.push(fn(element)) ;
                    });
                    return result;
                };


                var parseData = function(item) {
                    var firstColum18nStr = "Organisaatio ja kirjetyyppi";
                    var newRow = {};

                    //TODO handle localization
                    if(item.nimi && item.nimi.fi) {
                        newRow[firstColum18nStr] = item.nimi.fi;
                    } else if (item.nimi && item.nimi.sv) {
                        newRow[firstColum18nStr] = item.nimi.sv;
                    } else if (item.nimi && item.nimi.en) {
                        newRow[firstColum18nStr] = item.nimi.en;
                    }

                    newRow["lang"] = item.language;
                    newRow["status"] = item.state;
                    if(item.children && item.children.length > 0) {
                        var childrenRows = map(parseData, item.children);
                        newRow["children"] = childrenRows;
                    }
                    return newRow;
                };

                var newData = [];
                var dataArr = [];
                dataArr.push(response.data[0]);
                newData = map(parseData, dataArr);
                return newData;
            }
        }
    }]);