'use strict';

angular.module('letter-templates')
    .controller('TemplateDialogCtrl', ['$scope', '$modalInstance', 'TemplateService', '$state',
        function($scope, $modalInstance, TemplateService, $state) {

            $scope.languageSelection = 'FI';
            $scope.applicationTypeSelection = 'admission';

            $scope.applicationTarget = TemplateService.getApplicationTarget();
            TemplateService.getApplicationTargets().then(function(data) {
                $scope.applicationTargets = data;
            });

            //Show a list of applicationTargets that have the selected type
            $scope.previousApplicationTargets = [
                'Korkeakoulujen yhteishaku syksy 2013',
                'Korkeakoulujen yhteishaku syksy 2012'
            ];

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