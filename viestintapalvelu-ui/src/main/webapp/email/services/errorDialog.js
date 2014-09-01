'use strict';

angular.module('email')
.factory('ErrorDialog', ['$modal',
  function($modal) {
    return {
      showError : function(msg) {
        return $modal.open({
          templateUrl: './email/views/dialog.html',
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