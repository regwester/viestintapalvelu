'use strict';

angular.module('letter-templates')
    .controller('TemplateDialogCtrl', ['$scope', '$modalInstance', 'TemplateService', '$state',
        function($scope, $modalInstance, TemplateService, $state) {

            //Default values
            $scope.applicationTarget = TemplateService.getApplicationTarget();
            $scope.languageSelection = 'FI';
            //$scope.applicationTypeSelection = '';
            //$scope.baseTemplate = '';

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

            function getTemplate() {
                return {
                    name: $scope.applicationTarget.name,
                    language: $scope.languageSelection,
                    type: $scope.applicationTypeSelection,
                    oid: $scope.applicationTarget.value,
                    base: $scope.baseTemplate
                }
            }

            $scope.confirm = function() {
                $modalInstance.close();
                TemplateService.setTemplateInfo(getTemplate());
                $state.go('letter-templates.create');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }
    ]);