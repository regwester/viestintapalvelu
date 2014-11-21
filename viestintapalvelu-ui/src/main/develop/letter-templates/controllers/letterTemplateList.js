'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateListCtrl', ['$scope', '$modal', 'TemplateService',
        function($scope, $modal, TemplateService) {
            $scope.radioSelection = 'default';
            $scope.showTable = true;

            TemplateService.getApplicationTargets().then(function(data) {
                $scope.applicationTargets = data;
            });

            $scope.changeTarget = function() {
                TemplateService.getByApplicationPeriod($scope.applicationTarget).success(function(data) {
                    $scope.letterTemplates = data;
                });
                TemplateService.setApplicationTarget($scope.applicationTarget);
            };

            $scope.changeRadio = function() {
                ($scope.radioSelection === 'organization') ? $scope.showTable = false : $scope.showTable = true;
            };

            $scope.openCreateDialog = function() {
                $modal.open({
                    size: 'lg',
                    templateUrl: 'views/letter-templates/views/partials/new.html',
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
                },{
                    value: 'organization',
                    text: 'Organisaatioiden kirjeluonnokset'
                }
            ];
        }
    ]);