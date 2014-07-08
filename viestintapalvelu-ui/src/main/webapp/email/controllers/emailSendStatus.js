'use strict';

angular.module('email')
.controller('EmailSendStatusCtrl', ['$scope', '$rootScope', 'GroupEmail', '$state', '$timeout', 'ErrorDialog',
  function($scope, $rootScope, GroupEmail, $state, $timeout, ErrorDialog) {

    $scope.emailsendid = $rootScope.emailsendid;
    $scope.getStatus = function () {
      $scope.SendingStatusDTO = GroupEmail.status.save($scope.emailsendid.id).$promise.then(
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
      //				$state.path("/response");
      //				$timeout.cancel($rootScope.promise);
      //			}
      //			return ((ok + notOk +1) / (all +1)) * 100;
      //		};
  
      $scope.percentage = function (ok, notOk, all) {
        if (all == ok + notOk) {
          $state.go("email_response");
          $timeout.cancel($rootScope.promise);
        }
        return { width: (((ok + notOk +1) / (all +1)) * 100)  + '%' };
      };
  
      $rootScope.promise = $timeout($scope.getStatus, 3000);
    };
  
    $scope.statisticsEmail = function () {
      $timeout.cancel($rootScope.promise);
      $state.go("email_response");
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
