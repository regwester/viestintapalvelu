'use strict';

angular.module('report').controller('ReportedMessageListCtrl',
    function ReportedMessageListCtrl($scope, $state, GetReportedMessagesByOrganization,
        GetReportedMessagesBySearchArgument, SharedVariables, ErrorDialog) {
		$scope.pagination = {
			page: 1,
			pageSize: 10
		};
        $scope.sortedBy = 'sendingStarted';
        $scope.order = 'asc';
        $scope.descending = false;
        $scope.form = {
        	organization: '', 
        	searchArgument: ''
        };

        /**
         * Haetaan raportoitavat viestit
         */
        $scope.fetch = function() {
            // Hakutekijää ei ole annettu. Listalle haetut sanomat.
            if (SharedVariables.getSearchArgumentValue() == '') {
            	if (SharedVariables.getSelectedOrganizationValue() != '') {
            		$scope.form.organization = SharedVariables.getSelectedOrganizationValue();
            	}
            	
                GetReportedMessagesByOrganization.get({orgOid: $scope.form.organization.oid, 
                	nbrofrows: $scope.pagination.pageSize, page: $scope.pagination.page}, 
                function(result) {
                    $scope.reportedMessagesDTO = result;
                }, function(error) {
                    ErrorDialog.showError(error);
                });
            // Hakutekijä annettu. Poimitaan hakutekijä yhteisistä tiedoista ja suoritetaan haku.
            } else {
                $scope.form.organization = SharedVariables.getSelectedOrganizationValue();
                $scope.form.searchArgument = SharedVariables.getSearchArgumentValue();
                
                GetReportedMessagesBySearchArgument.get({orgOid: $scope.form.organization.oid, 
                    searchArgument: $scope.form.searchArgument, nbrofrows: $scope.pagination.pageSize, 
                    page: $scope.pagination.page}, 
                function(result) {
                    $scope.reportedMessagesDTO = result;
                }, function(error) {
                    ErrorDialog.showError(error);
                });
            }
        };
        
        /**
         * Haetaan tiedot lajiteltuna
         */
        $scope.fetchWithSorting = function() {
            // Hakutekijää ei ole annettu. Haetaan kaikki lajiteltuna.
            if (SharedVariables.getSearchArgumentValue() == '') {
                GetReportedMessagesByOrganization.get({orgOid: $scope.form.organization.oid, 
                    nbrofrows: $scope.pagination.pageSize, page: $scope.pagination.page, sortedby: $scope.sortedBy, 
                    order: $scope.order}, 
                function(result) {
                    $scope.reportedMessagesDTO = result;
                }, function(error){
                    ErrorDialog.showError(error);
                });
            // Hakutekijä annettu. Poimitaan hakutekijä kontrolloreiden yhteisistä tiedoista ja suoritetaan haku.
            } else {
                $scope.form.organization = SharedVariables.getSelectedOrganizationValue();
                $scope.form.searchArgument = SharedVariables.getSearchArgumentValue();
                GetReportedMessagesBySearchArgument.get(
                    {orgOid: $scope.form.organization.oid, searchArgument: $scope.form.searchArgument, 
                    nbrofrows: $scope.pagination.pageSize, page: $scope.pagination.page, sortedby: $scope.sortedBy, 
                    order: $scope.order}, 
                function(result) {
                    $scope.reportedMessagesDTO = result;
                }, function(error) {
                    ErrorDialog.showError(error);
                });
            }
        };
        
        /**
         * Hae-painiketta painettu
         */
        $scope.search = function() {
            // Viedään tiedot yhteisiin tietoihin 
            SharedVariables.setSearchArgumentValue($scope.form.searchArgument);
            SharedVariables.setSelectedOrganizationValue($scope.form.organization);
            // Suoritetaan haku
            GetReportedMessagesBySearchArgument.get({orgOid: $scope.form.organization.oid,
                searchArgument: $scope.form.searchArgument, nbrofrows: $scope.pagination.pageSize, 
                page: $scope.pagination.page}, 
            function(result) {
                $scope.reportedMessagesDTO = result;
            }, function(error) {
                ErrorDialog.showError(error);
            });
            // Näytettäväksi sivuksi ensimmäinen sivu.
            $scope.pagination.page = 1;
        };
            
        /**
         * Tyhjennä-painiketta painettu
         */
        $scope.clean = function() {
            // Tyhjennetään hakutekijä yhteisissä tiedoissa ja näytöllä
            SharedVariables.setSearchArgumentValue('');
            $scope.form.searchArgument = '';
            // Haetaan kaikkitiedot
            $scope.pagination.page = 1;
            $scope.fetch();
        };

        /**
         *  Palautetaan lajittelutyyli
         */
        $scope.getSortClass = function(headerName) {
            var className = '';
            if ($scope.sortedBy === headerName) {
            	className = 'sort-' + $scope.order;
            }
            return className;
        };
        
        /**
         *  Listan otskkoa klikattua lajittelua varten
         */
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
            
            // Asetetaan sivuksi ensimmäinen ja haetaan sanomat lajiteltuna
            $scope.pagination.page = 1; 
            $scope.fetchWithSorting();
        };
        
        /**
         * Valittiin seuraava tai edellinen sivu
         */
        $scope.pageChanged = function() {
            if ($scope.sortedBy != '') {
                $scope.fetchWithSorting();
            } else {
                $scope.fetch();
            }
        };

        /**
         * Organisaatio valittu vetovalikosta
         */
        $scope.selectOrganization = function() {
            // Viedään organisaatio yhteisiin tietoihin 
            SharedVariables.setSelectedOrganizationValue($scope.form.organization);
            // Suoritetaan haku
            GetReportedMessagesByOrganization.get({orgOid: $scope.form.organization.oid,
                nbrofrows: $scope.pagination.pageSize, page: $scope.pagination.page}, 
            function(result) {
                $scope.reportedMessagesDTO = result;
            }, function(error) {
                ErrorDialog.showError(error);
            });
            // Näytettäväksi sivuksi ensimmäinen sivu.
            $scope.pagination.page = 1;
        };      
        
        /**
         * Näytetään listalta valittu raportoitava viesti
         */
        $scope.showReportedMessage = function(reportedMessage) {
            $state.go('report_view', {messageID: reportedMessage.messageID});
        };
        
        // Alustetaan ensimmäinen sivu
        $scope.fetch();
});
