'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateListCtrl', ['$scope', '$modal', 'TemplateService',
        function($scope, $modal, TemplateService) {
            $scope.radioSelection = 'default';

            TemplateService.getDefaultTemplates().success(function(data){
                $scope.defaultTemplates = data;
            });

            TemplateService.getApplicationTargets().then(function(data) {
                var list = [];
                for(var i = 0, max = data.length; i < max; i++){
                    list.push({name: data[i].nimi.kieli_fi, value: data[i].oid});
                }
                $scope.letterTypes[1].list = list;
            });

            function updateTarget(applicationTarget) {
                TemplateService.getByApplicationPeriod(applicationTarget.oid)
                .success(function(data) {
                    $scope.$parent.applicationTemplates = data;
                }).error(function(e){

                });
                TemplateService.setApplicationTarget(applicationTarget);
            }

            $scope.getTemplates = function() {
                if($scope.radioSelection === 'default') {
                    return $scope.defaultTemplates;
                } else if($scope.radioSelection === 'applicationTarget') {
                    return $scope.applicationTemplates;
                }
            };

            $scope.changeRadio = function() {

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
                    text: 'Oletuskirjepohjat'
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