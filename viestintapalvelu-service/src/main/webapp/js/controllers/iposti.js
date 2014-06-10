'use strict';

angular.module('app')
.controller('IPostiCtrl', ['$scope', 'IPosti', '$window',
  function($scope, IPosti, $window){
		
	//Lataa lähettämättömät IPostit
	$scope.retrievePosts = function(){
		$scope.unsent = IPosti.unSentItems.query();
	};
	
	//Valitse IPosti
	$scope.selectPost = function(id) {
		$scope.selectedPost = $scope.unsent[id];
	};
	
	//Lähetä valittu IPosti
	$scope.sendPost = function() {
		IPosti.send.get({id:$scope.selectedPost.id});
		$scope.retrievePosts();
		$scope.selectedPost = {};
		$scope.showDetails = false;
	};
	
	//Lataa valitun IPostin sisältö
	$scope.downloadPost = function() {
		IPosti.getById($scope.selectedPost.id);
	};
	
	$scope.retrieveDetails = function() {
		$scope.details = IPosti.getDetailsById.query($scope.selectedPost.templateId, {id: $scope.selectedPost.templateId});
		$scope.showDetails = true;
	};
	
	$scope.unsentItemsExist = function() {
		if($scope.unsent.length > 0) {
			return true;
		}
		return false;
	};
	
  }
]);