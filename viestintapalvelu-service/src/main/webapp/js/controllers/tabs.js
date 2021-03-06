'use strict';

angular.module('app')
.controller('TabsCtrl', ['$scope',
  function ($scope) {

    $scope.tabs = [
       {name: 'Osoitetarrat', id:'osoitetarrat', content: getContentPath('osoitetarrat')},
       {name: 'Letter', id:'letter', content: getContentPath('letter'), active: true},
       {name: 'Mallien tuonti', id:'importtemplate', content: getContentPath('importtemplate')},
       {name: 'Hakulinkitys', id: 'applicationperiods', content:getContentPath('applicationperiods')},
       {name: 'Tulostus', id:'print', content: getContentPath('print')},
       {name: 'IPosti', id:'iposti', content: getContentPath('iposti')}
    ];

    function getContentPath(id){
    	return "html/"+id+".html";
    }
  }
]);