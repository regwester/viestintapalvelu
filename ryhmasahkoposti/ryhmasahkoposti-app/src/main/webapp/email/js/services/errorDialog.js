'use strict';

angular.module('viestintapalvelu')
.factory('ErrorDialog', ['$modal',
  function($modal) {
    return {
      showError : function(msg) {
        return $modal.open({
          templateUrl: './html/errorDialog.html',
          controller: 'ErrorDialogCtrl',
          size: 'lg',
          resolve: {
            msg: function() {
              return angular.copy(msg.data);
            }
          }
        });
      }
    };
  }
]);