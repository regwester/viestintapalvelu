
var services = angular.module('viestintapalvelu');

services.factory('GroupEmailFactory', function ($resource) {
	return $resource('/ryhmasahkoposti-service/email/sendGroupEmail', {}, {  
		  sendGroupEmail: { method: 'POST', isArray: false}
	  });
});


// Tästä alkaa testi
services.factory('EmailAttachmentFactory', function ($rootScope) {
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
        }
    };
});

services.directive('upload', ['EmailAttachmentFactory', function factory(EmailAttachmentFactory) {
    return {
        restrict: 'A',
        	link: function (scope, element, attrs) {
			            $(element).fileupload({
			                dataType: 'text',
			                add: function (e, data) {
			                	EmailAttachmentFactory.add(data);
			                },
			                progressall: function (e, data) {
			                    var progress = parseInt(data.loaded / data.total * 100, 10);
			                    EmailAttachmentFactory.setProgress(progress);
			                },
			                done: function (e, data) {
			                	EmailAttachmentFactory.setProgress(0);
			                }
			            });
        		  }
    };
}]);
// Tässä loppuu testi

//Tämä oli alkuperäinen	
//services.factory('EmailAttachmentFactory', function ($resource) {
//	  return $resource('/ryhmasahkoposti-service/email/loadEmailAttachment', {}, {  
//		  loadEmailAttachment: { method: 'POST', 
////			  					headers: {'Content-Type': 'application/x-www-form-urlencoded'},
////			  					headers: {'Content-Type':'application/x-www-form-urlencoded;multipart/form-data;'},
//			  					headers: {'Content-Type':'multipart/form-data;'},
////			  					headers: {'Content-Type': undefined},
////			  					data: {	file: $scope.attachment },			  					
//			  					isArray: false}
//	  })
//});


