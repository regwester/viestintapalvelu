'use strict';

angular.module('report').directive('reportedMessageStatus', function() {
  function link(scope) {
    scope.percentage = function(current, total) {
      if (total === 0) {
        return 0;
      }
      return Math.floor((current / (total)) * 100);
    };

    scope.isSendingComplete = function() {
      return scope.status.sendingEnded !== null;
    };

    scope.getTotalSendings = function() {
      var status = scope.status;
      return status.numberOfSuccesfulSendings + status.numberOfFailedSendings;
    };
  }

  return {
    restrict: 'E',
    replace: true,
    templateUrl: '/viestintapalvelu-ui/report/views/partials/reportedMessageStatus.html',
    scope: {
      'status': '=sendingStatus'
    },
    link: link
  }
});