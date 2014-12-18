'use strict';

angular.module('letter-templates')
    .controller('TemplateDialogCtrl', ['$scope', '$modalInstance', 'TemplateService', '$state',
        function($scope, $modalInstance, TemplateService, $state) {

            $scope.applicationTarget = TemplateService.getApplicationTarget();
            TemplateService.getApplicationTargets().then(function(data) {
                var list = [];
                for(var i = 0, max = data.length; i < max; i++){
                    list.push({name: data[i].nimi.kieli_fi, value: data[i].oid});
                }
                $scope.applicationTargets = list;
            });

            $scope.languages = [
                {value: 'FI', text: 'suomi'},
                {value: 'SV', text: 'ruotsi'},
                {value: 'EN', text: 'englanti'}
            ];
            $scope.applicationTypes = [
                {value: 'admission', text: 'Koekutsukirje'},
                {value: 'acceptance', text: 'Hyväksymiskirje'},
                {value: 'rejection', text: 'Jälkiohjauskirje'}];

            $scope.confirm = function() {
                $modalInstance.close();
                $state.go('letter-templates_create', {language: $scope.languageSelection, applicationType: $scope.applicationTypeSelection});
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }
    ]);