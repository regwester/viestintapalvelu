'use strict';

angular.module('letter-templates')
    .factory('PreviewService', ['$http', '$window', function ($http, $window) {

        return {
            previewPDF: function(templateId, state, content) {
                $http({
                    url: '/viestintapalvelu/api/v1/preview/letterbatch/pdf',
                    method: "POST",
                    data: {'templateId' : templateId,
                           'templateState' : state,
                           'letterContent' : content},
                    headers: {
                       'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    var blob = new Blob([data], {type: "application/pdf"});
                    var objectUrl = URL.createObjectURL(blob);
                    $window.open(objectUrl);
                }).error(function (data, status, headers, config) {

                });
            },
            previewLetter: function(templateId, state, content) {
                $http({
                    url: '/viestintapalvelu/api/v1/preview/letterbatch/email',
                    method: "POST",
                    data: {'templateId' : templateId,
                           'templateState' : state,
                           'letterContent' : content},
                    headers: {
                       'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    var blob = new Blob([data], {type: "text/plain"});
                    var objectUrl = URL.createObjectURL(blob);
                    $window.open(objectUrl);
                }).error(function (data, status, headers, config) {
                });
            }
        }
    }]);