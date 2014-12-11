'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateListCtrl', ['$scope', '$modal', '$filter', 'TemplateService',
        function($scope, $modal, $filter, TemplateService) {
            $scope.radioSelection = 'default';
            $scope.fetchDefaultTemplates = function() {
        	TemplateService.getDefaultTemplates().success(function(data){
        	    $scope.defaultTemplates = data;
        	});
            };
            
            $scope.fetchDefaultTemplates();

            TemplateService.getApplicationTargets().then(function(data) {
                var list = [];
                for (var i = 0, max = data.length; i < max; i++){
                    list.push({name: data[i].nimi.kieli_fi, value: data[i].oid});
                }
                $scope.letterTypes[1].list = list;
            });

            function updateTarget(applicationTarget) {
        	$scope.currentApplicationTarget = applicationTarget;
                TemplateService.getTemplatesByApplicationPeriod(applicationTarget.value)
                .success(function(data) {
                    $scope.$parent.applicationTemplates = data.publishedTemplates.concat(data.draftTemplates).concat(data.closedTemplates);
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
            
            $scope.updateTemplatesList = function() {
        	if ($scope.radioSelection === 'default') {
        	    return $scope.fetchDefaultTemplates();
        	} else if($scope.radioSelection === 'applicationTarget') {
        	    return updateTarget($scope.currentApplicationTarget);
        	}
            };
            
            $scope.parseTypes = function(template) {
        	if ($scope.radioSelection === 'default') {
        	    template.structure.cont
        	}
            }

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
            
            $scope.letterStates = {
        	    'julkaistu': $filter('i18n')('template.state.published'),
        	    'luonnos': $filter('i18n')('template.state.draft'),
        	    'suljettu': $filter('i18n')('template.state.closed')
            }
            
            $scope.removeTemplate = function(templateId, state) {
        	var modalInstance = $modal.open({
                    templateUrl: 'views/letter-templates/views/partials/removedialog.html',
                    controller: 'RemoveTemplateModalFromUse',
                    size: 'sm',
                    resolve: {
                	templateId: function () {
                            return templateId
                        },
                        state: function() {
                            return state
                        }
                    }
                })

                modalInstance.result.then(function() {
                    $scope.updateTemplatesList();
                });
            };
            
            $scope.publishTemplate = function(templateId, state) {
        	var modalInstance = $modal.open({
                    templateUrl: 'views/letter-templates/views/partials/publishdialog.html',
                    controller: 'PublishTemplate',
                    size: 'sm',
                    resolve: {
                	templateId: function () {
                            return templateId
                        },
        		state: function() {
        		    return state
        		}
                    }
                });

                modalInstance.result.then(function() {
                    $scope.updateTemplatesList();
                });
            };
        }
    ]);

angular.module('letter-templates').controller('RemoveTemplateModalFromUse', ['$scope', '$modalInstance', 'TemplateService', 'templateId', 'state', function ($scope, $modalInstance, TemplateService, templateId, state) {
    $scope.templateIdToRemove = templateId;
    $scope.state = state;

    $scope.cancel = function () {
	$modalInstance.dismiss('cancel');
    };

    $scope.remove = function () {
	TemplateService.getTemplateByIdAndState($scope.templateIdToRemove, $scope.state).then(function(result) {
	    var template = result.data
	    template.state = 'suljettu';
	    TemplateService.updateTemplate().put({}, template, function () {
		$modalInstance.close();
	    });
	});
    };

}]);

angular.module('letter-templates').controller('PublishTemplate', ['$scope', '$modalInstance', 'TemplateService', 'templateId', 'state', function ($scope, $modalInstance, TemplateService, templateId, state) {
    $scope.templateIdToPublish = templateId;
    $scope.state = state;
    
    $scope.cancel = function () {
	$modalInstance.dismiss('cancel');
    };

    $scope.remove = function () {
	TemplateService.getTemplateByIdAndState($scope.templateIdToPublish, $scope.state).then(function(result) {
	    var template = result.data
	    template.state = 'julkaistu';
	    TemplateService.updateTemplate().put({}, template, function () {
		$modalInstance.close();
	    });
	});
    };

}]);