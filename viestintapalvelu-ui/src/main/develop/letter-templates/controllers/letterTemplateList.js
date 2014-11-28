'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateListCtrl', ['$scope', '$modal', 'TemplateService',
        function($scope, $modal, TemplateService) {
            $scope.radioSelection = 'default';
            $scope.showTable = true;

            function updateTarget(applicationTarget) {
                TemplateService.getByApplicationPeriod(applicationTarget.oid)
                .success(function(data) {
                    $scope.$parent.letterTemplates = data;
                }).error(function(e){

                });
                TemplateService.setApplicationTarget(applicationTarget);
                console.log("Update");
            }

            TemplateService.getApplicationTargets().then(function(data) {
                $scope.letterTypes[1].list = data;
            });

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
                    text: 'Hakukohtaiset kirjepohjat',
                    update: updateTarget
                },{
                    value: 'organization',
                    text: 'Organisaatioiden kirjeluonnokset'
                }
            ];
        }
    ]);