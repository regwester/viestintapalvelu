'use strict';

angular.module('app').factory('Template', ['$http', '$window', function ($http, $window) {
    var templateUrl = 'api/v1/template/';

    return function () {
        function getNames() {
            return $http.get(window.url("viestintapalvelu.template.getNames"));
        }

        function getExampleFiles() {
            return $http.get(window.url("viestintapalvelu.template.exampleFiles"));
        }

        function getExample(name) {
            return $http.get(window.url("viestintapalvelu.template.exampleFile", name));
        }

        function saveTemplate(template) {
            return $http.post(window.url("viestintapalvelu.template.insert"), template);
        }

        function getByName(t) {
            return $http.get(window.urls().omitEmptyValuesFromQuerystring().url("viestintapalvelu.template.getByName", {
                templateName: t.name,
                languageCode: t.lang,
                applicationPeriod: t.applicationPeriod
            }));
        }

        function listVersionsByName(t, getContents, getPeriods) {
            var params = {
                templateName: t.name,
                languageCode: t.lang
            };
            if (getContents) {
                params.content="YES";
            }
            if (getPeriods) {
                params.periods="YES";
            }
            return $http.get(window.urls().omitEmptyValuesFromQuerystring().url("viestintapalvelu.template.listVersionsByName", params));
        }

        function getHistory(t, oid, applicationPeriod, tag) {
            return $http.get(window.urls("viestintapalvelu.template.getHistory", {
                templateName: t.name,
                languageCode: t.lang,
                oid: oid,
                applicationPeriod: applicationPeriod,
                tag: tag
            }));
        }

        function saveAttachedApplicationPeriods(templateId, applicationPeriods, useAsDefault) {
            return $http.put(window.url("viestintapalvelu.template.saveAttachedApplicationPeriods", {
                templateId: templateId,
                applicationPeriods: applicationPeriods,
                useAsDefault: useAsDefault
            }));
        }

        return {
            getNames: getNames,
            getExampleFiles: getExampleFiles,
            getExample: getExample,
            saveTemplate: saveTemplate,
            getByName: getByName,
            getHistory: getHistory,
            listVersionsByName: listVersionsByName,
            saveAttachedApplicationPeriods: saveAttachedApplicationPeriods
        };
    }();
}]);