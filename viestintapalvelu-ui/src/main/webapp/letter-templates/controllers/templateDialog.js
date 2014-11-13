'use strict';

angular.module('letter-templates')
    .controller('TemplateDialogCtrl', ['$scope', '$modalInstance',
        function($scope, $modalInstance) {

            $scope.languageSelection;
            $scope.applicationTypeSelection;

            $scope.applicationTargets = [
                'Korkeakoulujen yhteishaku syksy 2014',
                'Korkeakoulujen yhteishaku syksy 2015'
            ];
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
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }
    ]);