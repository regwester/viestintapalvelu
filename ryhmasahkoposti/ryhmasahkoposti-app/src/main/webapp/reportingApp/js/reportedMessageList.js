'use strict';

reportingApp.controller('ReportedMessageListController',
	function ReportedMessageListController($scope, $location, GetReportedMessages, GetReportedMessagesBySearchArgument, 
		SharedVariables) {
		$scope.pageSize = 5;
		$scope.currentPage = 1;
		$scope.sortedBy = '';
		$scope.order = 'asc';
		$scope.descending = false;

		/**
		 * Haetaan raportoitavat viestit
		 */
		$scope.fetch = function() {
			// Hakutekijää ei ole annettu. Listalle haetut sanomat.
			if (SharedVariables.getSearchArgumentValue() == '') {
			    $scope.reportedMessagesDTO = GetReportedMessages.get({nbrofrows: $scope.pageSize, page: $scope.currentPage});
			// Hakutekijä annettu. Poimitaan hakutekijä kontrolloreiden yhteisistä tiedoista ja suoritetaan haku.
			} else {
				$scope.searchArgument = SharedVariables.getSearchArgumentValue();
		    	$scope.reportedMessagesDTO = GetReportedMessagesBySearchArgument.get(
		    		{searchArgument: SharedVariables.getSearchArgumentValue(), 
		    		nbrofrows: $scope.pageSize,	page: $scope.currentPage});
			}
		};
		
	    /**
	     * Haetaan tiedot lajiteltuna
	     */
	    $scope.fetchWithSorting = function() {
			// Hakutekijää ei ole annettu. Haetaan kaikki lajiteltuna.
			if (SharedVariables.getSearchArgumentValue() == '') {
			    $scope.reportedMessagesDTO = GetReportedMessages.get({nbrofrows: $scope.pageSize, page: $scope.currentPage, 
			    	sortedby: $scope.sortedBy, order: $scope.order});
			// Hakutekijä annettu. Poimitaan hakutekijä kontrolloreiden yhteisistä tiedoista ja suoritetaan haku.
			} else {
				$scope.searchArgument = SharedVariables.getSearchArgumentValue();
		    	$scope.reportedMessagesDTO = GetReportedMessagesBySearchArgument.get(
		    		{searchArgument: SharedVariables.getSearchArgumentValue(), 
		    		sortedby: $scope.sortedBy, order: $scope.order});
			}	    	
	    };
	    
		/**
		 * Hae-painiketta painettu
		 */
	    $scope.search = function(searchArgument) {
	    	// Viedään hakutekijä yhteisiin tietoihin ja suoritetaan haku
	    	SharedVariables.setSearchArgumentValue(searchArgument);
	    	$scope.reportedMessagesDTO = GetReportedMessagesBySearchArgument.get({searchArgument: searchArgument, 
	    		nbrofrows: $scope.pageSize,	page: $scope.currentPage});
	    	// Näytettäväksi sivuksi ensimmäinen sivu.
	    	$scope.currentPage = 1;
	    };
	    	
	    /**
	     * Tyhjennä-painiketta painettu
	     */
	    $scope.clean = function() {
	    	// Tyhjennetään hakutekijä yhteisissä tiedoissa ja näytöllä
	    	SharedVariables.setSearchArgumentValue('');
	        $scope.searchArgument = '';
	        // Haetaan kaikkitiedot
	        $scope.reportedMessagesDTO = $scope.selectPage(1);
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
	    
	    // Alustetaan ensimmäinen sivu
	    $scope.selectPage(1);
});
