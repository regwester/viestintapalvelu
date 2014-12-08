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
      return status.numberOfSuccessfulSendings + status.numberOfFailedSendings;
    };
  }

  return {
    restrict: 'E',
    replace: true,
    templateUrl: 'views/report/views/partials/reportedMessageStatus.html',
    scope: {
      'status': '=sendingStatus'
    },
    link: link
  }
})
.directive('ngEnter', function() {
  return function(scope, element, attrs) {
    element.bind("keydown keypress", function(event) {
      if(event.which === 13) {
        scope.$apply(function(){
          scope.$eval(attrs.ngEnter);
        });
        event.preventDefault();
      }
    });
  };
});