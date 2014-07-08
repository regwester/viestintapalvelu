'use strict';

angular.module('report')
.controller('ReportedMessageViewCtrl', 
    function ReportedMessageViewCtrl($scope, $stateParams, $state, 
        GetReportedMessagesBySearchArgument, ReportedMessageAndRecipients, ReportedMessageAndRecipientsSendingUnsuccesful, 
        ErrorDialog) {
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
            ReportedMessageAndRecipients.get({messageID: $stateParams.messageID, 
                nbrofrows: $scope.pageSize,	page: $scope.currentPage}, 
            function(result) {
                $scope.reportedMessageDTO = result;
            }, function(error) {
                ErrorDialog.showError(error);
            });
        };

        // Haetaan raportoitava viesti lajiteltuna otsikkosarakkeen mukaan
        $scope.fetchWithSort = function() {
            ReportedMessageAndRecipients.get({messageID: $stateParams.messageID, 
                nbrofrows: $scope.pageSize,	page: $scope.currentPage, sortedby: $scope.sortedBy, order: $scope.order}, 
            function(result) {
                $scope.reportedMessageDTO = result;	
            }, function(error) {
                ErrorDialog.showError(error);
            });
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
            if ($scope.sortedBy != '') {
                $scope.fetchWithSort();
            } else {
                $scope.fetch();
            }
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
            $scope.fetchWithSort();
        };

        // Alustetaan ensimmäinen sivu
        $scope.selectPage(1);
    }
);