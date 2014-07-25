'use strict';

angular.module('report')
.controller('ReportedLetterViewCtrl', ['$scope', '$state','$stateParams', '$http', function ($scope, $state, $stateParams, $http) {
  var reportedLetterUrl = '/viestintapalvelu/api/v1/reporting/view';

  $scope.pagination = {
    page: 1,
    pageSize: 10
  };

  $scope.back = function() {
    $state.go('letter_reports');
  };

  $scope.fetch = function() {
    var params = {
      id: $stateParams.letterBatchID,
      page: $scope.pagination.page,
      nbrofrows: $scope.pagination.pageSize
    };

    $http.get(reportedLetterUrl, { params: params })
      .success(function(reportedLetter) {
        $scope.reportedLetterDTO = reportedLetter;
      })
      .error(function(err) {
        console.log(err);
      });
  };

  $scope.getLetterContent = function() {
    var template, content = '';

    function isContent(candidate) {
      return candidate.name === 'sisalto';
    }

    if ($scope.reportedLetterDTO && $scope.reportedLetterDTO.template) {
      template = $scope.reportedLetterDTO.template;
      if (template.replacements) {
        content = template.replacements.filter(isContent)[0].defaultValue;
      }
    }
    return content;
  };

  $scope.fetch();

}]);
