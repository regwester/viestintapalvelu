'use strict';

angular.module('report').directive('reportedMessageStatus', function() {
  function link(scope) {
    scope.percentage = function(current, total) {
      if (total === 0) {
        return 0;
      }
      return Math.floor((current / (total)) * 100);
    };

    scope.getTotalSendings = function getTotalSendings() {
      var status = scope.status;
      return status.numberOfSuccesfulSendings + status.numberOfFailedSendings;
    };

    scope.getETA = function() {
      var status = scope.status,
          elapsedTime, timePerSending, remainderApproximation,
          ETA = '';

      function toMinutes(ms) {
        return Math.floor((ms / 1000) / 60);
      }
      
      if (!isSendingComplete()) {
        if (getTotalSendings() > 0) {
          elapsedTime = status.sendingEnded - status.sendingStarted;
          timePerSending = elapsedTime / getTotalSendings();
          remainderApproximation = timePerSending * (status.nbrOfReciepients - getTotalSendings());
          ETA = toMinutes(remainderApproximation);
        }
      }

      return ETA; // placeholder implementation
    };

    scope.isSendingComplete = function isSendingComplete() {
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