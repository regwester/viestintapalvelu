'use strict';

angular.module('report')
.controller('ReportedMessageViewCtrl', 
    ['$scope', '$stateParams', '$state', '$interval', 'ReportedMessageAndRecipients', 'ReportedMessageAndRecipientsSendingUnsuccesful', 'ErrorDialog', 
    function ReportedMessageViewCtrl($scope, $stateParams, $state, $interval, ReportedMessageAndRecipients, ReportedMessageAndRecipientsSendingUnsuccesful, ErrorDialog) {
        var polling; // promise returned by $interval

        $scope.pageSize = 10;
        $scope.currentPage = 1;
        $scope.sortedBy = '';
        $scope.order = 'asc';
        $scope.descending = false;
        $scope.showSendingUnsuccesfulClicked = false;

        // Takaisinpainike painettu
        $scope.back = function() {
            $state.go('report');
        };

        // Haetaan raportoitava viesti
        $scope.fetch = function() {
            // compose the parameter object for AJAX call
            var parameters = {
                messageID: $stateParams.messageID,
                nbrofrows: $scope.pageSize,
                page: $scope.currentPage
            };
            if ($scope.sortedBy.length > 0) { // include sortedBy and order if sorting is activated
                angular.extend(parameters, { sortedby: $scope.sortedBy, order: $scope.order });
            }

            ReportedMessageAndRecipients.get(parameters, 
            function(result) {
                $scope.reportedMessageDTO = result;
            }, function(error) {
                ErrorDialog.showError(error);
            });
        };

        function fetchIfSending() {
            // if reportedMessagesDTO is not set, then wait for initial fetch to complete
            if ($scope.reportedMessageDTO) { 
                if (angular.isNumber($scope.reportedMessageDTO.endTime)) {
                    // no need to refresh is sending is complete
                    $interval.cancel(polling); 
                } else {
                    // fetch latest report
                    $scope.fetch();
                }
            } 
        };

        // Painettiin katso lähetys epäonnistunut painiketta
        $scope.showSendingUnsuccesful = function() {
            $scope.showSendingUnsuccesfulClicked = true;
            ReportedMessageAndRecipientsSendingUnsuccesful.get({messageID: $stateParams.messageID, 
                nbrofrows: $scope.pageSize,	page: $scope.currentPage}, 
            function(result) {
                $scope.reportedMessageDTO = result;
            }, function(error) {
                ErrorDialog.showError(error);
            });
        };

        // Sivuvalintaa painettu
        $scope.selectPage = function(page) {
            $scope.currentPage = page;
            $scope.fetch();
        };

        // Otsikkosaraketta klikattu. Palautetaan tyyliksi sort-true tai sort-false.
        $scope.clickHeader = function(headerName) {
            return headerName == $scope.sortedBy && 'sort-' + $scope.descending;
        };

        // Lajittelu otsikkosarakkeen mukaan
        $scope.sort = function(headerName) {
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
            $scope.currentPage = 1;
            $scope.fetch();
        };

        // Alustetaan ensimmäinen sivu
        $scope.selectPage(1);
        // start polling the server for updates
        polling = $interval(fetchIfSending, 1000);
    }
]);