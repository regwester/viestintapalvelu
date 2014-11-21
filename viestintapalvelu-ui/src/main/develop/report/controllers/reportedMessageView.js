'use strict';

angular.module('report').controller('ReportedMessageViewCtrl',
  ['$scope', '$stateParams', '$state', '$interval', 'ReportedMessageAndRecipients',
    'ReportedMessageAndRecipientsSendingUnsuccessful', 'ErrorDialog',
    function ReportedMessageViewCtrl($scope, $stateParams, $state, $interval, ReportedMessageAndRecipients, ReportedMessageAndRecipientsSendingUnsuccessful, ErrorDialog) {
      var polling, // promise returned by $interval
        POLLING_INTERVAL = 5 * 1000; // 5 seconds

      $scope.pagination = {
        page: 1,
        pageSize: 10
      };
      $scope.sortedBy = '';
      $scope.order = 'asc';
      $scope.descending = false;
      $scope.showSendingUnsuccessfulClicked = false;

      // Takaisinpainike painettu
      $scope.back = function () {
        $state.go('report');
      };

      // Muodostetaan hakuparametrit
      $scope.buildSearchParameters = function () {
        var parameters = {
          messageID: $stateParams.messageID,
          nbrofrows: $scope.pagination.pageSize,
          page: $scope.pagination.page
        };

        if ($scope.sortedBy.length > 0) { // include sortedBy and order if sorting is activated
          angular.extend(parameters, { sortedby: $scope.sortedBy, order: $scope.order });
        }

        return parameters;
      };

      // Haetaan raportoitava viesti
      $scope.fetch = function () {
        // compose the parameter object for AJAX call
        var parameters = {
          messageID: $stateParams.messageID,
          nbrofrows: $scope.pagination.pageSize,
          page: $scope.pagination.page
        };
        if ($scope.sortedBy.length > 0) { // include sortedBy and order if sorting is activated
          angular.extend(parameters, { sortedby: $scope.sortedBy, order: $scope.order });
        }

        ReportedMessageAndRecipients.get(parameters,
          function (result) {
            $scope.reportedMessageDTO = result;
          }, function (error) {
            ErrorDialog.showError(error);
          });
      };

      // Haaetaan vastaanottajat joille viestin lähetys on epäonnistunut
      $scope.fetchUnsuccessfulSendings = function () {
        var parameters = $scope.buildSearchParameters();

        ReportedMessageAndRecipientsSendingUnsuccessful.get(parameters,
          function (result) {
            $scope.reportedMessageDTO = result;
          }, function (error) {
            ErrorDialog.showError(error);
          });
      };

      function fetchIfSending() {
        // if reportedMessagesDTO is not set, then wait for initial fetch to complete
        if ($scope.reportedMessageDTO) {
          if ($scope.reportedMessageDTO.sendingStatus.sendingEnded != null) {
            // no need to refresh is sending is complete
            $interval.cancel(polling);
          } else {
            // fetch latest report
            $scope.fetch();
          }
        }
      };

      // Painettiin näytä kaikki linkkiä
      $scope.showAll = function () {
        $scope.showSendingUnsuccessfulClicked = false;
        $scope.sortedBy = '';
        $scope.order = 'asc';
        $scope.fetch();
      };

      // Painettiin näytä epäonnistuneet linkkiä
      $scope.showSendingUnsuccessful = function () {
        $scope.showSendingUnsuccessfulClicked = true;
        $scope.fetchUnsuccessfulSendings();
      };

      // Otsikkosaraketta klikattu. Palautetaan tyyliksi sort-true tai sort-false.
      $scope.clickHeader = function (headerName) {
        return headerName == $scope.sortedBy && 'sort-' + $scope.descending;
      };

      // Painettu seuraava sivu painiketta
      $scope.pageChanged = function () {
        if ($scope.showSendingUnsuccessfulClicked == false) {
          $scope.fetch();
        } else {
          $scope.showSendingUnsuccessful();
        }
      };

      // Lajittelu otsikkosarakkeen mukaan
      $scope.sort = function (headerName) {
        // Painettiin samaa otsikkosaraketta kuin aiemmin. Vaihdetaan järjestystä.
        if (headerName == $scope.sortedBy) {
          $scope.descending = !$scope.descending;
          // Painettiin uutta saraketta. Alustetaan lajittelu.
        } else {
          $scope.sortedBy = headerName;
          $scope.descending = false;
          $scope.order = '';
        }
        // Asetetaan palvelulle välitettävä lajittelujärjestys
        if (!$scope.descending) {
          $scope.order = 'asc';
        } else {
          $scope.order = 'desc';
        }
        // Asetetaan sivuksi ensimmäinen ja haetaan sanoman tiedot lajiteltuna
        $scope.pagination.page = 1;
        $scope.fetch();
      };

      // Alustetaan ensimmäinen sivu
      $scope.fetch();
      // start polling the server for updates
      polling = $interval(fetchIfSending, POLLING_INTERVAL);
    }
  ]);