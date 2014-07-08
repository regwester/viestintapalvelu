'use strict';

angular.module('report').directive('reportedMessageStatus', function() {
  function link(scope) {
    scope.percentage = function(current, total) {
      if (total === 0) {
        return 0;
      }
      return Math.floor((current / (total)) * 100);
    };

    scope.getTotalSendings = function() {
      var status = scope.status;
      return status.numberOfSuccesfulSendings + status.numberOfFailedSendings;
    };

    scope.getETA = function() {
      return "ETA"; // placeholder implementation
    };

    scope.isSendingComplete = function() {
      return scope.status.sendingEnded !== null;
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