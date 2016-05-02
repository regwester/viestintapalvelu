'use strict';

angular.module('app').factory('Printer', ['$http', '$window', function ($http, $window) {
    var letter = 'api/v1/letter/';
    var printurl = 'api/v1/printer/';
    var download = 'api/v1/download/';

    return function () {
        function osoitetarratPDF(labels) {
            print(window.url("viestintapalvelu.addresslabel", 'pdf'), {"addressLabels": labels})
        }

        function osoitetarratXLS(labels) {
            print(window.url("viestintapalvelu.addresslabel", 'xls'), {"addressLabels": labels})
        }

        function letterPDF(letters, replacements, tName, tLang, oid, applicationPeriod, tag) {
            print(window.url("viestintapalvelu.letter", 'pdf'), {
                "letters": letters, "templateReplacements": replacements, "templateName": tName, "languageCode": tLang, "organizationOid": oid, "applicationPeriod": applicationPeriod, "tag": tag});
        }

        function buildLetter(letters, replacements, tName, tLang, oid, applicationPeriod, tag, iposti) {
            var letter = {
                "letters": letters,
                "templateReplacements": replacements,
                "templateName": tName,
                "languageCode": tLang,
                "organizationOid": oid,
                "applicationPeriod": applicationPeriod,
                "tag": tag
            };
            if (iposti) {
                letter.iposti = true;
            }
            return letter;
        }

        function asyncPdf(letters, replacements, tName, tLang, oid, applicationPeriod, tag) {
            return $http.post(window.url("viestintapalvelu.letterAsync", "pdf"), buildLetter(letters, replacements, tName, tLang, oid, applicationPeriod, tag))
                .error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Async PDF -kutsu epäonnistui: " + data);
                });
        }

        function doDownload(id) {
            return function () {
                $window.location.href = window.url("dokumenttipalvelu-service.lataa", id.batchId);
            }
        }

        function asyncZip(letters, replacements, tName, tLang, oid, applicationPeriod, tag) {
            return $http.post(window.url("viestintapalvelu.letterAsync", "zip"), buildLetter(letters, replacements, tName, tLang, oid, applicationPeriod, tag))
                .error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Async zip -kutsu epäonnistui: " + data);
                });
        }

        function asyncLetter(letters, replacements, tName, tLang, oid, applicationPeriod, tag, iposti) {
            return $http.post(window.url("viestintapalvelu.letterAsync", "letter"), buildLetter(letters, replacements, tName, tLang, oid, applicationPeriod, tag, iposti)).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Async letter -kutsu epäonnistui: " + data);
                });
        }

        function asyncStatus(id) {
            return $http.get(window.url("viestintapalvelu.letterAsyncStatus", id.batchId)).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Async status -kutsu epäonnistui: " + data);
                });
        }

        function languageOptions(id) {
            return $http.get(window.url("viestintapalvelu.letterLanguageOptions", id.batchId)).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Kielivaihtoehtojen haku epäonnistui: " + data);
                });
        }

        function sendEmail(id) {
            return $http.post(window.url("viestintapalvelu.letterEmailLetterBatch", id.batchId)).
                error(function (data) {
                    // This is test-ui so we use a popup for failure-indication against guidelines (for production code)
                    $window.alert("Sähköpostiviestin lähetys kirjelähetykselle " + id + " epäonnistui: " + data);
                });
        }

        function previewEmail(id, langCode) {
            if (langCode) {
                var lc = langCode;
                var url = window.url("viestintapalvelu.letterPreviewLetterBatchEmail", id.batchId, {language:lc});
                $http.get(url).success(function () {
                    $window.location = url;
                }).error(function () {
                    $window.alert("Ei löytynyt kielellä " + lc);
                });
            } else {
                $window.location = window.url("viestintapalvelu.letterPreviewLetterBatchEmail", id);
            }
        }

        function letterZIP(letters, replacements, tName, tLang, oid, applicationPeriod, tag) {
            print(letter + 'zip', {
                "letters": letters, "templateReplacements": replacements, "templateName": tName, "languageCode": tLang, "organizationOid": oid, "applicationPeriod": applicationPeriod, "tag": tag});
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
            asyncPdf: asyncPdf,
            asyncZip: asyncZip,
            asyncStatus: asyncStatus,
            doDownload: doDownload,
            sendEmail: sendEmail,
            previewEmail: previewEmail,
            languageOptions: languageOptions
        }
    }()
}])
