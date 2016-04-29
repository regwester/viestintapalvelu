'use strict';

angular.module('letter-templates')
    .factory('PreviewService', ['$http', '$window', function ($http, $window) {

        return {
            previewPDF: function(templateId, state, content) {
                $http({
                    url: window.url("viestintapalvelu.preview.letterbatch", "pdf"),
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
                    saveAs(blob, "preview.pdf");
                }).error(function (data, status, headers, config) {

                });
            },
            previewLetter: function(templateId, state, content) {
                $http({
                    url: window.url("viestintapalvelu.preview.letterbatch", "email"),
                    method: "POST",
                    data: {'templateId' : templateId,
                           'templateState' : state,
                           'letterContent' : content},
                    headers: {
                       'Content-type': 'application/json'
                    },
                    responseType: 'arraybuffer'
                }).success(function (data, status, headers, config) {
                    var blob = new Blob([data], {type: "text/plain;charset=utf-8"});
                    saveAs(blob, "preview.eml");
                }).error(function (data, status, headers, config) {
                
                });
            }
        }
    }]);