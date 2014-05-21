var services = angular.module('viestintapalvelu');

// --- Resources ---
services.factory('GroupEmailInitFactory', function($resource) {
	return $resource('/ryhmasahkoposti-service/email/initGroupEmail', {}, {
		initGroupEmail: {method: 'GET', isArray: false}
	});
});

services.factory('GroupEmailFactory', function ($resource) {
	return $resource('/ryhmasahkoposti-service/email/sendGroupEmail', {}, {  
		  sendGroupEmail: { method: 'POST', isArray: false}
	  });
});

services.factory('EmailSendStatusFactory', function ($resource) {
	return $resource('/ryhmasahkoposti-service/email/sendEmailStatus', {}, {  
		sendEmailStatus: { method: 'POST', isArray: false}
	  });
});

services.factory('EmailResultFactory', function ($resource) {
	return $resource('/ryhmasahkoposti-service/email/sendResult', {}, {  
		sendResult: { method: 'POST', isArray: false}
	  });
});


//--- Upload ---
services.factory('uploadManager', function ($rootScope) {
    var _files = [];
    return {
        add: function (file) {
            file.submit();
        },
        setProgress: function (percentage) {
            $rootScope.$broadcast('uploadProgress', percentage);
        },
        setResult : function(result) { 
            $rootScope.$broadcast('fileLoaded', result);      	
        }
    };
});


services.directive('upload', ['uploadManager', function factory(uploadManager) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            $(element).fileupload({
                dataType: 'json',
                add: function (e, data) {
                    uploadManager.add(data);
                },
                progressall: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    uploadManager.setProgress(progress);
                },
                done: function (e, data) {
                    uploadManager.setResult(data.result);
                	uploadManager.setProgress(0);
                }
            });
        }
   };
}]);

