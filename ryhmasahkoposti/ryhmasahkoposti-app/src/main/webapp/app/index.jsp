<%@ page import="org.jsoup.*" %>
<%@ page import="org.jsoup.safety.*" %>
<!DOCTYPE html>
<html id="ng-app" ng-app="viestintapalvelu">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>OPH - Viestintäpalvelu</title>
</head>

<body>
	<div id="application-wrapper">
	    <div id="angularbox" ng-view=""></div>
	</div>
   	
    <!-- libs -->
    <script type="text/javascript" src="./lib/jquery/jquery-1.10.2.min.js"></script>    
    <script type="text/javascript" src="./lib/jquery/jquery.i18n.properties-min-1.0.9.js"></script>
    <script type="text/javascript" src="./lib/jQuery-File-Upload-9.5.2/vendor/jquery.ui.widget.js"></script>

    <script type="text/javascript" src="./lib/angular/angular.js"></script> 
    <script type="text/javascript" src="./lib/angular/angular-resource.js"></script>
    <script type="text/javascript" src="./lib/angular/angular-route.js"></script>
    <script type="text/javascript" src="./lib/angular/angular-animate.js"></script>
    
    <script type="text/javascript" src="./lib/jQuery-File-Upload-9.5.2/jquery.fileupload.js"></script>
    <script type="text/javascript" src="./lib/jQuery-File-Upload-9.5.2/jquery.iframe-transport.js"></script>
    <script type="text/javascript" src="./lib/jQuery-File-Upload-9.5.2/jquery.fileupload-ui.js"></script>

    <!--Virkailija layout script -->
    <script type="text/javascript" src="/virkailija-raamit/apply-raamit.js"></script>


	<%
		String emailData = request.getParameter("emailData");
		if (emailData != null) {
			// Sanitize data:
			emailData = Jsoup.clean(emailData, Whitelist.relaxed());
			//System.out.println(emailData);
		} else {
			// no emaildata found.
			emailData = "";
		}
	%>

    <!-- app js-->
    <script src="./js/emailLocalization.js"></script>
    
	<script type="text/javascript">
			
		var email = angular.module('viestintapalvelu', ['localization', 'ngRoute', 'ngResource']);
		
		email.config(['$routeProvider',  function ($routeProvider) {
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
			$scope.emaildata = <%= emailData %>;
			
			$scope.showTo  = $scope.emaildata.recipient.length <= 30;
			$scope.showCnt = $scope.emaildata.recipient.length >  30;
					
			$scope.sendGroupEmail = function () {
				
				$scope.emailsendid = GroupEmailFactory.sendGroupEmail($scope.emaildata).$promise.then(
		            function(value) {
		                $rootScope.emailsendid = value;                 
		    			$location.path("/status");                        
		            },
		            function(error) {
		                alert("Virhe: Ei valtuuksia lähettää. \nSisäänkirjautuminen puuttuu/puutteellinen.");
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
		//        alert("Poista " + id);
			    $scope.emaildata.email.attachInfo.splice(id, 1);
		    };
			    	    
		}]);
	</script>
	<!--  script src="./js/email.js"></script>-->
    <script src="./js/emailCancel.js"></script>
    <script src="./js/emailSendStatus.js"></script>
    <script src="./js/emailService.js"></script>
    <script src="./js/emailResponse.js"></script>
    
    <!-- css -->
    <link rel="stylesheet" href="./css/bootstrap-combined.css"/>
    <link rel="stylesheet" href="./css/virkailija.css"/>
    
</body>