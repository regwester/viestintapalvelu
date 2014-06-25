'use strict';

angular.module('app')
.controller('IPostiCtrl', ['$scope', 'IPosti', '$window',
  function($scope, IPosti, $window){

    //Lataa lähettämättömät Batchit
    $scope.retrieveBatches = function(){
      $scope.unsent = IPosti.unSentItems.query();
    };
    
    $scope.selectBatch = function(id) {
      $scope.selectedBatch = $scope.unsent[id];
    };

    $scope.sendBatch = function() {
      IPosti.sendBatch.get({id:$scope.selectedBatch.id}, function(){
        refreshView();
      });
    };

    $scope.sendMail = function() {
      IPosti.sendMail.get({id:$scope.selectedBatch.ipostiId}, function(){
        refreshView();
      });
    };

    function refreshView() {
      $scope.retrieveBatches();
      $scope.selectedBatch = {};
      $scope.showDetails = false;
    }

    $scope.uploadCustomBatch = function() {
      var f = document.getElementById('file').files[0],
          r = new FileReader();

      r.onloadend = function(e) {
        var batch = e.target.result;
        IPosti.sendBatch.post({batch: batch});
      };
      r.readAsArrayBuffer(f);
    };

    //Batch on zip tiedosto, joka sisältää kokoelman pdf-muodossa olevia kirjeita ja metadataa.
    $scope.downloadBatch = function() {
      IPosti.getBatchById($scope.selectedBatch.id);
    };

    //IPosti on zip tiedosto, joka koostuu 1-n batchista.
    $scope.downloadMail = function() {
      IPosti.getIPostiById($scope.selectedBatch.ipostiId);
    };

    $scope.retrieveDetails = function() {
      $scope.details = IPosti.getDetailsById.query({letterBatchId: $scope.selectedBatch.ipostiId}, function() {
        $scope.showDetails = true;
      });
    };

    $scope.unsentBatchesExist = function() {
      if($scope.unsent.length > 0) {
        return true;
      }
      return false;
    };

  }
]);