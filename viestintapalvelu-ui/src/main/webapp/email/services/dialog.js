'use strict';

angular.module('viestintapalvelu')
  .factory('DialogService', ['$modal',
    function($modal) {

      var defaults = {
        templateUrl: '/email/views/partials/dialog.html',
        size: 'lg'
      };

      return {
        showDialog : function(opts) {
          return $modal.open({
            templateUrl: (opts.templateUrl ? opts.templateUrl : defaults.templateUrl),
            controller: (opts.controller ? opts.controller : defaults.controller),
            size: (opts.size ? opts.size : defaults.size),
            resolve: {
              msg: function() {
                return opts.msg;
              }
            }
          });
        },
        showErrorDialog: function(opts) {
          return $modal.open({
            templateUrl: 'email/views/partials/errorDialog.html',
            controller: 'errorDialog'
          })
        }
      };
    }
  ]);
