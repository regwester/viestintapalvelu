'use strict';

angular.module('app')
.controller('TabsCtrl', ['$scope',
  function ($scope) {

    $scope.tabs = [
       {name: 'Osoitetarrat', id:'osoitetarrat', content: getContentPath('osoitetarrat')},
       {name: 'Jälkiohjauskirje', id:'jalkiohjauskirje', content: getContentPath('jalkiohjauskirje')},
       {name: 'Hyväksymiskirje', id:'hyvaksymiskirje', content: getContentPath('hyvaksymiskirje')},
       {name: 'Koekutsukirje', id:'koekutsukirje', content: getContentPath('koekutsukirje')},
       {name: 'Letter', id:'letter', content: getContentPath('letter'), active: true},
       {name: 'Mallien tuonti', id:'importtemplate', content: getContentPath('importtemplate')},
       {name: 'Tulostus', id:'print', content: getContentPath('print')},
       {name: 'IPosti', id:'iposti', content: getContentPath('iposti')}
    ];

    function getContentPath(id){
    	return "html/"+id+".html";
    }
  }
]);