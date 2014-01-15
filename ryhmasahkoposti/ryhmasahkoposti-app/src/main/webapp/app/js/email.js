var email = angular.module('viestintapalvelu', ['ngRoute', 'ngResource']);

email.config(['$routeProvider',  function ($routeProvider) {
//		alert("email.config");
		$routeProvider.when('/', {templateUrl: '/ryhmasahkoposti-app/app/html/email.html', controller: 'EmailController'});
		$routeProvider.when('/response/', {templateUrl: '/ryhmasahkoposti-app/app/html/emailResponse.html', controller: 'EmailResponseController'});
		$routeProvider.when('/cancel/', {templateUrl: '/ryhmasahkoposti-app/app/html/emailCancel.html', controller: 'EmailCancelController'});
	    $routeProvider.otherwise({redirectTo: '/'});
}]);


email.controller('EmailController', ['$scope', '$rootScope', 'EmailAttachmentFactory', 'GroupEmailFactory' , '$location', 
                                     function($scope, $rootScope, EmailAttachmentFactory, GroupEmailFactory, $location) { 	
//	alert("EmailController");
	
	$scope.emailresponse = [];
	
	$scope.emaildata = {
			headers: [
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
					body: 'Testi bodya ja sporttia.'
			}
		};
	
		$scope.showTo  = $scope.emaildata.headers.length <= 30;
		$scope.showCnt = $scope.emaildata.headers.length >  30;
		
	
	// Tästä alkaa testi
		$scope.files = [];
		$scope.percentage = 0;	
	
		$scope.upload = function () {
			alert("Upload pressed");
			EmailAttachmentFactory.upload();
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
	// Tässä loppuu testi
		
		$scope.sendGroupEmail = function () {
//			alert("sendGroupEmail mail pressed");
			$location.path("/response");
			
			$scope.emailresponse = GroupEmailFactory.sendGroupEmail($scope.emaildata);					
			$rootScope.emailresponse = $scope.emailresponse;
		};

		
		$scope.cancelEmail = function () {
//			alert("cancelEmail mail pressed" );
			$location.path("/cancel");

			$rootScope.callingProcess = $scope.emaildata.headers[0].callingProcess;			
		};
	
}]);

