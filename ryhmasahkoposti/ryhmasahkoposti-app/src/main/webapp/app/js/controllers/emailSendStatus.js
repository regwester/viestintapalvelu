'use strict';

angular.module('viestintapalvelu')
.controller('EmailSendStatusCtrl', ['$scope', '$rootScope', 'EmailSendStatusFactory', '$location', '$timeout', 'ErrorDialog',
  function($scope, $rootScope, EmailSendStatusFactory, $location, $timeout, ErrorDialog) {

    $scope.emailsendid = $rootScope.emailsendid;
    $scope.getStatus = function () {
      $scope.SendingStatusDTO = EmailSendStatusFactory.sendEmailStatus($scope.emailsendid.id).$promise.then(
        function(value) {
          $scope.SendingStatusDTO = value; 
        },
        function(error) {
          ErrorDialog.showError(error);
        },
        function(update) {
          alert("Notification " + update);
        }
      );
  
      // Proper way but does not work in IE9, OK in Crome and Firefox
      //		$scope.percentage = function (ok, notOk, all) {
      //			if (all == ok + notOk) {
      //				$location.path("/response");
      //				$timeout.cancel($rootScope.promise);
      //			}
      //			return ((ok + notOk +1) / (all +1)) * 100;
      //		};
  
      $scope.percentage = function (ok, notOk, all) {
        if (all == ok + notOk) {
          $location.path("/response");
          $timeout.cancel($rootScope.promise);
        }
        return { width: (((ok + notOk +1) / (all +1)) * 100)  + '%' };
      };
  
      $rootScope.promise = $timeout($scope.getStatus, 3000);
    };
  
    $scope.statisticsEmail = function () {
      $timeout.cancel($rootScope.promise);
      $location.path("/response");
    };
  
    // Start the poll	
    $scope.$on('$viewContentLoaded', function() {
      $scope.getStatus();
    });
  
    // Stop the poll
    $scope.$on('$locationChangeStart', function() {
      $timeout.cancel($rootScope.promise);
    });
  
    // Stop the poll 2
    $scope.$on('$destroy', function() {
      $timeout.cancel($rootScope.promise);
    });
  }
]);
