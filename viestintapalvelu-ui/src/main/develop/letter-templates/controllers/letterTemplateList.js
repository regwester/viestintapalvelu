'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateListCtrl', ['$scope', '$modal', '$filter', '$state', 'TemplateService',
        function ($scope, $modal, $filter, $state, TemplateService) {

            var templateTabActive = true,
                draftTabActive = false,
                templatesUpdated = false,
                draftsUpdated = false,
                selectAppPeriod = "Select one";

            $scope.radioSelection = 'default';
            $scope.languageSelection = 'suomi';
            $scope.template_tree_control = {};
            $scope.draft_tree_control = {};
            $scope.applicationPeriodList = [];
            $scope.template_tree_data = [];
            $scope.draft_tree_data = [];
            $scope.selectedApplicationPeriod = selectAppPeriod;

            $scope.col_defs = [
                { field: "lang", displayName: "Kieli"},
                { field: "status", displayName: "Tila"}
            ];

            $scope.fetchDefaultTemplates = function () {
                TemplateService.getDefaultTemplates().success(function (data) {
                    $scope.defaultTemplates = data;
                });
            };

            $scope.fetchDefaultTemplates();

            $scope.updateTreeData = function(applicationPeriod) {

                var query = TemplateTreeService.getOrganizationHierarchy(applicationPeriod.oid);
                query.then(function(response){
                    var parsedTree = TemplateTreeService.getParsedTreeGrid(response);
                    var treedata = parsedTree.tree;
                    var organizationOIDS = parsedTree.oids;
                    var oidToRowMap = parsedTree.oidToRowMap;
                    if(templateTabActive) {
                        $scope.template_tree_data = treedata;
                        TemplateService.getTemplatesByOid(organizationOIDS).then(function(response) {
                            treedata = TemplateTreeService.addTemplatesToTree(treedata, response.data, oidToRowMap, $scope.template_tree_control);
                            $scope.template_tree_data = treedata;
                            templatesUpdated = true;
                        });
                    } else if (draftTabActive) {
                        $scope.draft_tree_data = treedata;
                        TemplateService.getDraftsByOid(applicationPeriod.oid, organizationOIDS).then(function(response){
                            treedata = TemplateTreeService.addTemplatesToTree(treedata, response.data, oidToRowMap, $scope.draft_tree_control);
                            $scope.draft_tree_data = treedata;
                            draftsUpdated = true;
                        });
                    }
                });
            };

            TemplateService.getApplicationTargets().then(function (data) {
                var list = [];
                for (var i = 0, max = data.length; i < max; i++) {
                    list.push({name: data[i].nimi.kieli_fi, value: data[i].oid});
                }
                $scope.letterTypes[1].list = list;
                $scope.applicationTargets = list;
            });

            function updateTarget(applicationTarget) {
                $scope.currentApplicationTarget = applicationTarget;
                TemplateService.getTemplatesByApplicationPeriod(applicationTarget.value)
                    .success(function (data) {
                        $scope.$parent.applicationTemplates = data.publishedTemplates.concat(data.draftTemplates).concat(data.closedTemplates);
                    }).error(function (e) {

                    });
                TemplateService.setApplicationTarget(applicationTarget);
            }

            $scope.getTemplates = function () {
                if ($scope.radioSelection === 'default') {
                    return $scope.defaultTemplates;
                } else if ($scope.radioSelection === 'applicationTarget') {
                    return $scope.applicationTemplates;
                }
            };

            $scope.updateTemplatesList = function () {
                if ($scope.radioSelection === 'default') {
                    return $scope.fetchDefaultTemplates();
                } else if ($scope.radioSelection === 'applicationTarget') {
                    return updateTarget($scope.currentApplicationTarget);
                }
            };

            $scope.openCreateDialog = function () {
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
                },
                {
                    value: 'applicationTarget',
                    text: 'Hakukohtaiset kirjepohjat',
                    update: updateTarget
                },
                {
                    value: 'organization',
                    text: 'Organisaatioiden kirjeluonnokset'
                }
            ];

            $scope.letterStates = {
                'julkaistu': $filter('i18n')('template.state.published'),
                'luonnos': $filter('i18n')('template.state.draft'),
                'suljettu': $filter('i18n')('template.state.closed')
            };

            $scope.templateTab = function() {
                templateTabActive = true;
                draftTabActive = false;
                if(!templatesUpdated && $scope.selectedApplicationPeriod !== selectAppPeriod ) {
                    console.log("updating templates");
                    $scope.updateTreeData($scope.selectedApplicationPeriod);
                }
            };

            $scope.draftTab = function() {
                templateTabActive = false;
                draftTabActive = true;
                if(!draftsUpdated && $scope.selectedApplicationPeriod !== selectAppPeriod) {
                    console.log("updating drafts");
                    $scope.updateTreeData($scope.selectedApplicationPeriod);

                }
            };

            $scope.editTemplate = function (templateId) {
                $state.go('letter-templates_edit', {'templateId': templateId});
            };

            $scope.removeTemplate = function (templateId, state) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/letter-templates/views/partials/removedialog.html',
                    controller: 'RemoveTemplateModalFromUse',
                    size: 'sm',
                    resolve: {
                        templateId: function () {
                            return templateId
                        },
                        state: function () {
                            return state
                        }
                    }
                });

                modalInstance.result.then(function () {
                    $scope.updateTemplatesList();
                });
            };

            $scope.publishTemplate = function (templateId, state) {
                var modalInstance = $modal.open({
                    templateUrl: 'views/letter-templates/views/partials/publishdialog.html',
                    controller: 'PublishTemplate',
                    size: 'sm',
                    resolve: {
                        templateId: function () {
                            return templateId
                        },
                        state: function () {
                            return state
                        },
                        template: function () {
                            return null
                        }
                    }
                });

                modalInstance.result.then(function () {
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
        TemplateService.getTemplateByIdAndState($scope.templateIdToRemove, $scope.state).then(function (result) {
            var template = result.data;
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
        TemplateService.getTemplateByIdAndState($scope.templateIdToPublish, $scope.state).then(function (result) {
            var template = result.data;
            template.state = 'julkaistu';
            TemplateService.updateTemplate().put({}, template, function () {
                $modalInstance.close();
            });
        });
    };

}]);
