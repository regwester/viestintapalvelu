angular.module('app').controller('LetterController', ['$scope', 'Generator', 'Printer', 'Template', '$timeout', 'Options', '_',
  function ($scope, Generator, Printer, Template, $timeout, Options, _) {
    $scope.letters = [];
    $scope.count = 0;
    $scope.select_min = 0; // Min count of letters selectable from UI drop-down list
    $scope.select_max = 100;  // Max count
    Template.getNames().success(function (data) {
      $scope.templates = data;
    });
    $scope.replacements = [];
    $scope.historyList = [];
    $scope.oid = "1.2.246.562.10.00000000001";
    $scope.applicationPeriod = "K2014";
    $scope.generalEmail = null;
    $scope.haut = [{nimi: {kieli_fi: "Listaa hauista ladataan..."}, oid:null}];

    Options.hakus(function(haut) {
        $scope.haut = haut;
    });

    $scope.templateChanged = function () {
      $scope.template.applicationPeriod = $scope.applicationPeriod;
      Template.getByName($scope.template).success(function (data) {
        $scope.replacements = data;
        for (i in data.replacements) {
          var r = data.replacements[i];
          if (r.name == "sisalto") {
            $scope.tinymceModel = r.defaultValue;
          }
        }
      });
      Template.getHistory($scope.template, $scope.oid, $scope.applicationPeriod, $scope.tag).success(function (data) {
        $scope.historyList = data;
      });
    };

    $scope.historyChanged = function () {
      for (i in $scope.history.templateReplacements) {
        var r = $scope.history.templateReplacements[i];
        if (r.name == "sisalto") {
          $scope.tinymceModel = r.defaultValue;
        }
      }
    };

    $scope.tinymceModel = '';
    function generateLetters(count) {
      $scope.letters = $scope.letters.concat(Generator.generateObjects(count, function (data) {
        var tulokset = generateTulokset(data.any('hakutoiveLukumaara'));
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
          "templateReplacements": {"tulokset": tulokset,
            "koulu": tulokset[0]['organisaationNimi'],
            "koulutus": tulokset[0]['hakukohteenNimi'],
            "muut_hakukohteet" : ["Muu hakukohde 1", "Muu hakukohde 2"]
          }
        };
      }));
    }

    function generateTulokset(count) {
      return Generator.generateObjects(count, function (data) {
        var selectionCriteria = data.randomItems('selectionCriteria', 3);
        return {
          "organisaationNimi": data.any('organisaationNimi'),
          "oppilaitoksenNimi": data.any('oppilaitoksenNimi'),
          "hakukohteenNimi": data.any('hakukohteenNimi'),
          "hyvaksytyt": data.any('hyvaksytyt'),
          "kaikkiHakeneet": data.any('kaikkiHakeneet'),
          "alinHyvaksyttyPistemaara": data.any('alinHyvaksyttyPistemaara'),
          "omatPisteet": data.any('pisteetvajaa'),  // Ei nivelkirjeessä
          "paasyJaSoveltuvuuskoe": data.any('koe'), // Ei nivelkirjeessä
          "valinnanTulos": data.any('valinnanTulos'),
          "varasija": data.any('varasija'),
          "hylkaysperuste": data.any('valinnanTulos'), // Ei nivelkirjeessä
          "sijoitukset": _.map(selectionCriteria, function(item) { //kk_haussa katsotaan monia erilaisia pisteitä (koe, valinta, jne..)
              return {'nimi': item, 'oma': data.any('pisteetvajaa'), 'alin': data.any('alinHyvaksyttyPistemaara')};
          }),
          "pisteet": _.map(selectionCriteria, function(item) { //kk_haussa katsotaan monia erilaisia pisteitä (koe, valinta, jne..)
              return {'nimi': item, 'oma': data.any('pisteetvajaa'), 'alin': data.any('alinHyvaksyttyPistemaara')};
          })
        };
      });
    }

   function replacements() {
       return  {"sisalto": $scope.tinymceModel,
           "hakukohde": "Tässä lukee hakukohde",
           "tarjoaja": "Tässä tarjoajan nimi",
           "koeaika": "12.12.2014 klo 12.12",
           "koepaikka": "TTY sali TB2012",
           "koepaikanosoite": "Korkeakoulunkatu 10,\n33720 Tampere",
           "hakijapalveluidenYhteystiedot": "Hakijapalvelut, PL 123, 10100 HELSINKI"
       };
   }

    $scope.generatePDF = function () {
      Printer.letterPDF($scope.letters,replacements(),
          $scope.template.name, $scope.template.lang, $scope.oid, $scope.applicationPeriod, $scope.tag);
    };

    $scope.monitorStatus = null;
    $scope.emailPossibleForId = null;
    $scope.emailPreviewPossibleForId = null;
    $scope.languageOptions = [];
    $scope.downloadAllowedForId = null;
    $scope.iposti = true;
    function startBatchMonitor(id, whenDone) {
        $scope.monitorStatus = null;
        $scope.emailPossibleForId = null;
        $scope.emailPreviewPossibleForId = null;
        $scope.languageOptions = [];
        $scope.downloadAllowedForId = null;

        var monitor = function() {
            Printer.asyncStatus(id).success(function(status) {
                if (!$scope.monitorStatus || status.sent != $scope.monitorStatus.sent) {
                    Printer.languageOptions(id).success(function(opts) {
                        $scope.languageOptions = opts.options;
                    });
                }
                $scope.monitorStatus = status;
                if (status.status == "ready") {
                    $scope.downloadAllowedForId = id;
                    if (status.emailReviewable) {
                        $scope.emailPossibleForId = id;
                        $scope.emailPreviewPossibleForId = id;
                    }
                    if (whenDone) {
                        whenDone();
                    }
                } else {
                    if (status.emailReviewable) {
                        $scope.emailPreviewPossibleForId = id;
                    }
                    $timeout(monitor, 100);
                }
            });
        };
        monitor();
    }

    $scope.sendEmail = function() {
       if ($scope.emailPossibleForId) {
           Printer.sendEmail($scope.emailPossibleForId).success(function() {
               $scope.emailPossibleForId = null;
               alert("Viesti lähti ryhmäsähköpostipalvelulle.")
           });
       }
    };

    $scope.previewEmail = function(langCode) {
        if ($scope.emailPreviewPossibleForId) {
            Printer.previewEmail($scope.emailPreviewPossibleForId, langCode);
        }
    };

    $scope.generateAsyncPdf = function() {
      Printer.asyncPdf($scope.letters,replacements(),
          $scope.template.name, $scope.template.lang, $scope.oid, $scope.applicationPeriod, $scope.tag)
          .success(function(id) {
              startBatchMonitor(id, Printer.doDownload(id));
        });
    };

    $scope.download = function() {
        if ($scope.downloadAllowedForId) {
            Printer.doDownload($scope.downloadAllowedForId)();
        }
    };

    $scope.generateAsyncZip = function() {
      Printer.asyncZip($scope.letters,replacements(),
          $scope.template.name, $scope.template.lang, $scope.oid, $scope.applicationPeriod, $scope.tag)
          .success(function(id) {
              startBatchMonitor(id, Printer.doDownload(id));
          });
    };

    $scope.generateAsyncLetter = function() {
      Printer.asyncLetter($scope.letters,replacements(),
          $scope.template.name, $scope.template.lang, $scope.oid, $scope.applicationPeriod, $scope.tag,
                $scope.iposti)
          .success(function(id) {
              startBatchMonitor(id);
          });
    };

    $scope.fillEmails = function() {
       angular.forEach($scope.letters, function(letter) {
          letter.emailAddress = $scope.generalEmail;
       });
    };

    $scope.generateZIP = function () {
      Printer.letterZIP($scope.letters, replacements(),
          $scope.template.name, $scope.template.lang, $scope.oid, $scope.applicationPeriod, $scope.tag);
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

  }
]);

