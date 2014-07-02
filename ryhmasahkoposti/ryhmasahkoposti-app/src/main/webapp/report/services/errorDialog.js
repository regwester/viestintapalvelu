'use strict';

angular.module('report')
.factory('ErrorDialog',
    function($modal) {
        return {
            showError : function(msg) {
                if (msg.status == '403') {
                    msg.data = 'error.msg.userNotAuthorized';
                }
                
                return $modal.open({
                    templateUrl: './report/views/errorDialog.html',
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
);