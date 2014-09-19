'use strict';

angular.module('report')
.controller('ReportedLetterViewCtrl', ['$scope', '$state','$stateParams', '$http', '$window', function ($scope, $state, $stateParams, $http, $window) {
  var seriviceAPIUrl = '/viestintapalvelu/api/v1'
    , reportingAPIUrl = seriviceAPIUrl + '/reporting'
    , reportedLetterUrl = reportingAPIUrl + '/view';

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

  $scope.downloadLetter = function(recipient) {
    var getLetterDownloadLink, letterDownloadLinkUrl = reportingAPIUrl + '/letter';

    getLetterDownloadLink = $http.get(letterDownloadLinkUrl, { params: { id: recipient.letterReceiverLetterID } });
    getLetterDownloadLink.success(function(downloadLink) {
      // changing window location will initiate download prompt
      // TODO: the backend should return only the documentID, not the whole url
      //       this way naturally exposes the user to security risks
      var acceptableDownloadLink = $window.location.origin + seriviceAPIUrl + '/download/';
      if (downloadLink.indexOf(acceptableDownloadLink) === 0) {
        $window.location = downloadLink;
      }
    });
  };

  $scope.downloadContents = function(letterBatchID) {
	  var getContentsDownloadLink; 
	  var contentsDownloadLinkUrl = reportingAPIUrl + '/contents';

	  getContentsDownloadLink = $http.get(contentsDownloadLinkUrl, {params: {id: letterBatchID}});
	  getContentsDownloadLink.success(function(downloadLink) {
		  var acceptableDownloadLink = $window.location.origin + seriviceAPIUrl + '/download/';
		  if (downloadLink.indexOf(acceptableDownloadLink) === 0) {
			  $window.location = downloadLink;
		  }
	  });
  };

  $scope.sendIPosti = function(mailID) {
    var sendIPostiUrl = seriviceAPIUrl + '/iposti/sendMail/' + mailID;
    $http.get(sendIPostiUrl);
  };

  $scope.fetch();

}]);
