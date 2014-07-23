'use strict';

angular.module('report')
.controller('ReportedLetterListCtrl', ['$scope', '$http', function($scope, $http) {
  var reportedLettersListUrl = '/viestintapalvelu/api/v1/reporting/list',
      reportedLettersSearchUrl = '/viestintapalvelu/api/v1/reporting/search';

  $scope.pagination = {
    page: 1,
    pageSize: 10
  };

  // used to make sure the right option is selected after fetch happens
  function setSelectedOrganizationOid() {
    var index = $scope.reportedLettersDTO.selectedOrganization
      , org = $scope.reportedLettersDTO.organizations[index];
    $scope.selectedOrganizationOid = org.oid;
  }

  $scope.fetch = function() {
    var params = {}, url = reportedLettersListUrl;
    if ($scope.organisationOid) {
      params.orgOid = $scope.selectedOrganizationOid;
    }
    if ($scope.searchArgument) {
      params.searchArgument = $scope.searchArgument;
      url = reportedLettersSearchUrl;
    }

    angular.extend(params, {
      page: $scope.pagination.page,
      nbrofrows: $scope.pagination.pageSize
    });

    $http.get(url, {params: params})
      .success(function(reportedLettersDTO) {
        $scope.reportedLettersDTO = reportedLettersDTO;
        setSelectedOrganizationOid();
      })
      .error(function(err) {
        console.log(err);
      });
  };

  $scope.search = function() {
    // reset page for fetch
    $scope.pagination.page = 1;
    $scope.fetch(); 
  };

  $scope.showReportedLetter = function(letterBatch) {
    console.log(letterBatch);
  };

  $scope.emptySearch = function() {
    $scope.searchArgument = '';
    $scope.pagination.page = 1;
    $scope.fetch();
  };

  $scope.fetch();

}]);
