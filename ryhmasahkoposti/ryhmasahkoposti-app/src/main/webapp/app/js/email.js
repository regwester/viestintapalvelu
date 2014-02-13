var email = angular.module('viestintapalvelu', ['ngRoute', 'ngResource']);

email.config(['$routeProvider',  function ($routeProvider) {
//		$routeProvider.when('/test/', {templateUrl: '/ryhmasahkoposti-app/app/html/test.html', controller: 'TestController'});
		$routeProvider.when('/email', {templateUrl: '/ryhmasahkoposti-app/app/html/email.html', controller: 'EmailController'});
		$routeProvider.when('/cancel/', {templateUrl: '/ryhmasahkoposti-app/app/html/emailCancel.html', controller: 'EmailCancelController'});
		$routeProvider.when('/status/', {templateUrl: '/ryhmasahkoposti-app/app/html/emailSendStatus.html', controller: 'EmailSendStatusController'});
		$routeProvider.when('/response/', {templateUrl: '/ryhmasahkoposti-app/app/html/emailResponse.html', controller: 'EmailResponseController'});
	    $routeProvider.otherwise({redirectTo: '/email'});
}]);

email.controller('EmailController', ['$scope', '$rootScope', 'GroupEmailFactory' ,'uploadManager', '$location', 
                                     function($scope, $rootScope, GroupEmailFactory, uploadManager, $location) { 	

	$rootScope.emailsendid = "";

	$scope.emaildata = "";
	
	$scope.emaildata = {
			recipient: [
					{oid: '1234567890ABCD',
					oidType: 'henkilo',
					email: 'ville.vastaanottaja@gmail.com',
					languageCode: 'FI'},
								
					{oid: 'ABCD0987654321',
					oidType: 'henkilo',
					email: 'torspo.uolevi@gmail.com',
					languageCode: 'FI'}	
			],		
			email: {callingProcess: 'Osoitetietojarjestelma',
					ownerEmail: 'oph_tiedotus@oph.fi',
					senderEmail: 'Mikko.Mallikas@oph.fi',
					senderOid: '11223344556677',
					senderOidType: 'henkilo',
					subject: 'Testi viesti',
					body: 'Testi bodya ja sporttia.',
					attachInfo: []
			}
		};
	
	// For testing
	for(var i=0; i<0; i++) {
		$scope.emaildata.recipient.push(
				{oid: '1234567890ABCD',
				oidType: 'henkilo',
				email: 'herra_' +i+ '.vastaanottaja@gmail.com',
				languageCode: 'FI'}	);
		}
	// For testing
	
	$scope.showTo  = $scope.emaildata.recipient.length <= 30;
	$scope.showCnt = $scope.emaildata.recipient.length >  30;
			
	$scope.sendGroupEmail = function () {
		
		$scope.emailsendid = GroupEmailFactory.sendGroupEmail($scope.emaildata).$promise.then(
            function(value) {
                $rootScope.emailsendid = value;                 
    			$location.path("/status");                        
            },
            function(error) {
                alert("Error " + error);
            },
            function(update) {
                alert("Notification " + update);
            }
        );
	};
					
	
	$scope.cancelEmail = function () {
		$location.path("/cancel");

		$rootScope.callingProcess = $scope.emaildata.email.callingProcess;			
	};
			
	
	$scope.files = [];
    $scope.percentage = 0;
	
	// Upload -->	
    $scope.upload = function () {
    	uploadManager.upload();
        $scope.files = [];
    };

    $rootScope.$on('fileAdded', function (e, call) {
        $scope.files.push(call);
        $scope.$apply();
    });

    $rootScope.$on('uploadProgress', function (e, call) {
        $scope.percentage = call;
        $scope.$apply();
    });
    
    $rootScope.$on('uploadDone', function (e, call) {
    	$scope.$apply();
    });
    
    $rootScope.$on('fileLoaded', function (e, call) {
        $scope.emaildata.email.attachInfo.push(call);
        $scope.$apply();
    });
    
    //--- 'Poista' ---	    
    $scope.remove = function(id) {
	    $scope.emaildata.email.attachInfo.splice(id, 1);
    };
	    	    
}]);

