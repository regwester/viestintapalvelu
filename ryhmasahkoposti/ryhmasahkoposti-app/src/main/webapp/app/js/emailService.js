var services = angular.module('viestintapalvelu');

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

// ----- //

services.factory('uploadManager', function ($rootScope) {
    var _files = [];
    return {
        add: function (file) {
            _files.push(file);
            $rootScope.$broadcast('fileAdded', file.files[0].name);
        },
        clear: function () {
            _files = [];
        },
        files: function () {
            var fileNames = [];
            $.each(_files, function (index, file) {
                fileNames.push(file.files[0].name);
            });
            return fileNames;
        },
        upload: function () {
            $.each(_files, function (index, file) {
                file.submit();
            });
            this.clear();
            
        },
        setProgress: function (percentage) {
            $rootScope.$broadcast('uploadProgress', percentage);
        },
        setResult : function(result) {
//        	alert("result " + result);
            $rootScope.$broadcast('fileLoaded', result);
        	
        }
    };
});


services.directive('upload', ['uploadManager', function factory(uploadManager) {
    return {
        restrict: 'A',
        link: function (scope, element, attrs) {
            $(element).fileupload({
//                dataType: 'text',
                dataType: 'json',
                add: function (e, data) {
                    uploadManager.add(data);
                },
                progressall: function (e, data) {
                    var progress = parseInt(data.loaded / data.total * 100, 10);
                    uploadManager.setProgress(progress);
                },
                done: function (e, data) {
//                    alert("done:" + data.result);
                    uploadManager.setResult(data.result);
                	uploadManager.setProgress(0);
                }
            });
        }
   };
}]);

