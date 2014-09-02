'use strict';

angular.module('viestintapalvelu')
  .factory('DialogService', ['$modal',
    function($modal) {
      /* Define a set of default values for the modal. */
      var defaults = {
        size: 'lg',
        templateUrl: 'email/views/partials/dialog.html',
        controller: 'DialogCtrl'
      };

      /* Override those default values where a custom value was given. */
      function getOptions(customOpts) {
        var opts = {};
        angular.extend(opts, defaults, customOpts);
        return opts;
      }

      function openDialog(size, fn) {
        var customOpts = {size: size, resolve: { opts: fn }};
        return $modal.open(getOptions(customOpts));
      }

      return {
        showConfirmationDialog : function(msg, confirm, size) {
          var fn = function() {
            return { type: 'confirm', msg: msg, confirm: confirm }
          };
          openDialog(size, fn);
        },
        showErrorDialog: function(msg, size) {
          var fn = function() {
            return { type: 'error', msg: msg };
          };
          openDialog(size, fn);
        },
        showInfoDialog: function(msg, size) {
          var fn = function() {
            return { type: 'info', msg: msg };
          };
          openDialog(size, fn);
        }
      };
    }
  ]);
