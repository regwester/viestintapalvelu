'use strict';

reportingApp.controller('ReportedMessageListController',
	function ReportedMessageListController($scope, $location, GetReportedMessagesByOrganization,
		GetReportedMessagesBySearchArgument, SharedVariables, ErrorDialog) {
		$scope.pageSize = 10;
		$scope.currentPage = 1;
		$scope.sortedBy = '';
		$scope.order = 'asc';
		$scope.descending = false;
		$scope.form = {organization: '', searchArgument: ''};

		/**
		 * Haetaan raportoitavat viestit
		 */
		$scope.fetch = function() {
			// Hakutekijää ei ole annettu. Listalle haetut sanomat.
			if (SharedVariables.getSearchArgumentValue() == '') {
			    GetReportedMessagesByOrganization.get({orgOid: $scope.form.organization.oid, nbrofrows: $scope.pageSize, 
			    	page: $scope.currentPage}, 
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
		    		searchArgument: $scope.form.searchArgument,	nbrofrows: $scope.pageSize,	page: $scope.currentPage}, 
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
			    	nbrofrows: $scope.pageSize, page: $scope.currentPage, sortedby: $scope.sortedBy, order: $scope.order}, 
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
		    		nbrofrows: $scope.pageSize, page: $scope.currentPage, sortedby: $scope.sortedBy, order: $scope.order}, 
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
	    		searchArgument: $scope.form.searchArgument, nbrofrows: $scope.pageSize,	page: $scope.currentPage}, 
	    	function(result) {
	    		$scope.reportedMessagesDTO = result;	
	    	}, function(error) {
		    	ErrorDialog.showError(error);	    		
	    	});
	    	// Näytettäväksi sivuksi ensimmäinen sivu.
	    	$scope.currentPage = 1;
	    };
	    	
	    /**
	     * Tyhjennä-painiketta painettu
	     */
	    $scope.clean = function() {
	    	// Tyhjennetään hakutekijä yhteisissä tiedoissa ja näytöllä
	    	SharedVariables.setSearchArgumentValue('');
	        $scope.form.searchArgument = '';
	        // Haetaan kaikkitiedot
	        $scope.selectPage(1);
	    };

		/**
		 *  Otsikkosaraketta klikattu. Palautetaan tyyliksi sort-true tai sort-false.
		 */
		$scope.clickHeader = function(headerName) {
			return headerName == $scope.sortedBy && 'sort-' + $scope.descending;
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
			$scope.currentPage = 1;	
			$scope.fetchWithSorting();
	    };
	    
	    /**
	     * Valittiin seuraava tai edellinen sivu
	     */
	    $scope.selectPage = function(page) {
			$scope.currentPage = page;
			
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
	    		nbrofrows: $scope.pageSize, page: $scope.currentPage}, 
	    	function(result) {
	    		$scope.reportedMessagesDTO = result;	
	    	}, function(error) {
		    	ErrorDialog.showError(error);	    		
	    	});
	    	// Näytettäväksi sivuksi ensimmäinen sivu.
	    	$scope.currentPage = 1;
	    };	    
	    
	    /**
	     * Näytetään listalta valittu raportoitava viesti
	     */
	    $scope.showReportedMessage = function(reportedMessage) {
	    	$location.path("/reportMessages/view/" + reportedMessage.messageID);
	    };
	    
	    // Alustetaan ensimmäinen sivu
	    $scope.selectPage(1);
});
