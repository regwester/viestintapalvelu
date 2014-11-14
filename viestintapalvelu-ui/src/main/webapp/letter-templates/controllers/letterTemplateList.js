'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateListCtrl', ['$scope', '$modal', 'TemplateService',
        function($scope, $modal, TemplateService) {

            $scope.radioSelection;
            $scope.applicationTarget;

            TemplateService.getHakus().success(function(data) {
                $scope.applicationTargets = data;
            });

            $scope.selectTarget = function() {
                TemplateService.getByApplicationPeriod($scope.applicationTarget).success(function(data) {
                    $scope.letterTemplates = data;
                });
                /*[
                    {
                        applicationTarget: 'Korkeakoulujen yhteishaku syksy 2014',
                        type: 'Koekutsukirje',
                        languages: ['suomi', 'ruotsi'],
                        status: 'Keskeneräinen',
                        saveDate: '16.10.2140 10:17'
                    },{
                        applicationTarget: 'Korkeakoulujen yhteishaku syksy 2014',
                        type: 'Hyväksymistesti',
                        languages: ['suomi', 'ruotsi', 'englanti'],
                        status: 'Käytössä',
                        saveDate: '16.10.2140 10:17'
                    },{
                        applicationTarget: 'Korkeakoulujen yhteishaku syksy 2014',
                        type: 'Jälkiohjauskirje',
                        languages: ['suomi'],
                        status: 'Poistettu käytöstä',
                        saveDate: '16.10.2140 10:17'
                    }
                ];*/
            };

            $scope.openCreateDialog = function() {
                $modal.open({
                    size: 'lg',
                    templateUrl: 'letter-templates/views/partials/create.html',
                    controller: 'TemplateDialogCtrl'
                });
            };

            $scope.letterTypes = [
                {
                    value: 'default',
                    text: 'Oletuskirjepohja'
                },{
                    value: 'applicationTarget',
                    text: 'Hakukohtaiset kirjepohjat'
                }
            ];
        }
    ]);