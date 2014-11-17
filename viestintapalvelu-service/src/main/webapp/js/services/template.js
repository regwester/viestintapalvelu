'use strict';

angular.module('app').factory('Template', ['$http', '$window', function ($http, $window) {
    var templateUrl = 'api/v1/template/';

    return function () {
        function getNames() {
            return $http.get(templateUrl + 'getNames');
        }

        function getExampleFiles() {
            return $http.get(templateUrl + 'exampleFiles');
        }

        function getExample(name) {
            return $http.get(templateUrl + 'exampleFiles/' + name);
        }
        
        function getTemplateFiles() {
            return $http.get(templateUrl + 'partialFiles');
        }

        function getReplacementFiles() {
            return $http.get(templateUrl + 'replacementFiles');
        }

        function getStyleFiles() {
            return $http.get(templateUrl + 'styleFiles');
        }

        function createTemplate(params) {
            return $http.get(templateUrl + 'get', {params: params});
        }

        function saveTemplate(template) {
            return $http.post(templateUrl + 'insert', template);
        }

        function getByName(t) {
            var url = templateUrl + 'getByName?templateName=' + t.name + '&languageCode=' + t.lang;
            if (t.applicationPeriod) {
                url += "&applicationPeriod=" + t.applicationPeriod;
            }
            return $http.get(url);
        }

        function listVersionsByName(t, getContents, getPeriods) {
            var url = templateUrl + 'listVersionsByName?templateName=' + t.name + '&languageCode=' + t.lang;
            if (getContents) {
                url += "&content=YES";
            }
            if (getPeriods) {
                url += "&periods=YES";
            }
            return $http.get(url);
        }

        function getHistory(t, oid, applicationPeriod, tag) {
            if (tag != null && tag != "") {
                return $http.get(templateUrl + 'getHistory?templateName=' + t.name + '&languageCode=' + t.lang + '&oid=' + oid + "&applicationPeriod=" + applicationPeriod + "&tag=" + tag);
            } else {
                return $http.get(templateUrl + 'getHistory?templateName=' + t.name + '&languageCode=' + t.lang + '&oid=' + oid);
            }
        }

        function saveAttachedApplicationPeriods(templateId, applicationPeriods, useAsDefault) {
            return $http.put(templateUrl + "saveAttachedApplicationPeriods", {
                templateId: templateId,
                applicationPeriods: applicationPeriods,
                useAsDefault: useAsDefault
            });
        }

        return {
            getNames: getNames,
            getExampleFiles: getExampleFiles,
            getExample: getExample,
            getTemplateFiles: getTemplateFiles,
            getReplacementFiles: getReplacementFiles,
            getStyleFiles: getStyleFiles,
            createTemplate: createTemplate,
            saveTemplate: saveTemplate,
            getByName: getByName,
            getHistory: getHistory,
            listVersionsByName: listVersionsByName,
            saveAttachedApplicationPeriods: saveAttachedApplicationPeriods
        };
    }();
}]);