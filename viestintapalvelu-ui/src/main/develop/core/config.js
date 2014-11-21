'use strict';

angular.module('core')
.config(['paginationConfig', function(paginationConfig){

  //Override bootstrap pagination
  paginationConfig.boundaryLinks = false;
  paginationConfig.directionLinks = true;
  paginationConfig.firstText = 'Ensimmäinen'; //TODO: localize the texts
  paginationConfig.itemsPerPage = 10;
  paginationConfig.lastText = 'Viimeinen';
  paginationConfig.nextText = 'Seuraava';
  paginationConfig.previousText = 'Edellinen';
  paginationConfig.rotate = true;

}]);