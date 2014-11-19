'use strict';

angular.module('email')
  .directive('recipientList', [function factory() {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: 'views/partials/recipientList.html',
        scope: {
          'recipients': '=',
          'form': '=',
          'limit': '='
        }
      };
    }
  ]);