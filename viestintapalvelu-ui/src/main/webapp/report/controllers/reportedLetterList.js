'use strict';

angular.module('report')
.controller('ReportedLetterListCtrl', ['$scope', '$http', function($scope, $http) {
  var reportedLettersUrl = '/viestintapalvelu/api/v1/reporting/list';

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
    var params = {};
    if ($scope.organisationOid) {
      params.orgOid = $scope.selectedOrganizationOid;
    }

    angular.extend(params, {
      page: $scope.pagination.page,
      nbrofrows: $scope.pagination.pageSize
    });

    $http.get(reportedLettersUrl, {params: params})
      .success(function(reportedLettersDTO) {
        $scope.reportedLettersDTO = reportedLettersDTO;
        setSelectedOrganizationOid();
      })
      .error(function(err) {
        console.log(err);
      });
  };

  $scope.showReportedLetter = function(letterBatch) {
    console.log(letterBatch);
  };

  $scope.fetch();

}]);
