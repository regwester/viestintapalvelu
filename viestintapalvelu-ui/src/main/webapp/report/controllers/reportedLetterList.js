'use strict';

angular.module('report')
.controller('ReportedLetterListCtrl', ['$scope', '$state', '$http', function($scope, $state, $http) {
  var reportedLettersListUrl = '/viestintapalvelu/api/v1/reporting/list',
      reportedLettersSearchUrl = '/viestintapalvelu/api/v1/reporting/search';

  $scope.sortAndOrder = {
    sortedby: 'timestamp',
    order: 'desc'
  };

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
    }, $scope.sortAndOrder);

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

  $scope.sortBy = function(headerName) {
    var sortAndOrder = $scope.sortAndOrder;

    if (sortAndOrder.sortedby !== headerName) {
      sortAndOrder.sortedby = headerName;
      sortAndOrder.order = 'desc';
    } else {
      sortAndOrder.order = sortAndOrder.order === 'asc' ? 'desc' : 'asc';
    }

    $scope.pagination.page = 1;
    $scope.fetch();
  };

  $scope.showReportedLetter = function(reportedLetter) {
    $state.go('letter_batch_view', {letterBatchID: reportedLetter.letterBatchID});
  };

  $scope.emptySearch = function() {
    $scope.searchArgument = '';
    $scope.pagination.page = 1;
    $scope.fetch();
  };

  $scope.getSortClass = function(headerName) {
    var className = '';
    if ($scope.sortAndOrder.sortedby === headerName) {
      className = 'sort-' + $scope.sortAndOrder.order;
    }
    return className;
  };

  $scope.fetch();

}]);
