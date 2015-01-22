/*
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */

'use strict';

angular.module('letter-templates')
    .controller('TemplateController', ['$scope', '$state', 'TemplateService', 'TemplateTreeService', '$modal', function($scope, $state, TemplateService, TemplateTreeService, $modal) {

        $scope.templateTabActive = true;
        $scope.hakukohdeTabActive = false;
        $scope.branch = {};
        $scope.templatesUpdated = false;
        $scope.draftsUpdated = false;
        var selectAppPeriod = "Select one";
        $scope.showTreeLoadingIndicator = false;
        $scope.template_tree_control = {};
        $scope.draft_tree_control = {};
        $scope.applicationPeriodList = [];
        $scope.selectedApplicationPeriod = selectAppPeriod;
        $scope.col_defs = [
            { field: "language", displayName: "Kieli"},
            { field: "state", displayName: "Tila"}
        ];

        $scope.template_tree_data = [
            {"Organisaatio ja kirjetyyppi":"", lang: "", status:"", children:[]}
        ];

        $scope.draft_tree_data = [
            {"Organisaatio ja kirjetyyppi":"", lang: "", status:"", children:[]}
        ];

        $scope.user_oid = '';

        $scope.placeholder_valitse_haku = "Valitse haku";

        $scope.templateTab = function() {
            $scope.templateTabActive = true;
            $scope.hakukohdeTabActive = false;
            if(!$scope.templatesUpdated && $scope.selectedApplicationPeriod !== selectAppPeriod ) {
                $scope.updateTreeData($scope.selectedApplicationPeriod);
            }
        };

        $scope.draftTab = function() {
            $scope.templateTabActive = false;
            $scope.hakukohdeTabActive = true;
            if(!$scope.draftsUpdated && $scope.selectedApplicationPeriod !== selectAppPeriod) {
                $scope.updateTreeData($scope.selectedApplicationPeriod);

            }
        };

        $scope.$on("treeTemplateChanged", function(event, args) {
            $scope.draft = args;
        });

        $scope.my_tree_handler = function(branch, event){
        };

        $scope.updateTreeData = function(applicationPeriod) {
            $scope.showTreeLoadingIndicator=true;

            var query = TemplateTreeService.getOrganizationHierarchy(applicationPeriod.oid);
            query.then(function(response){
                var parsedTree = TemplateTreeService.getParsedTreeGrid(response);
                var treedata = parsedTree.tree;
                var organizationOIDS = parsedTree.oids;
                var oidToRowMap = parsedTree.oidToRowMap;
                var oids = [];
                organizationOIDS.forEach(function(element, index, array){
                    if(element.isLeaf) {
                        oids.push(element.oid);
                    }
                });

                if($scope.templateTabActive) {
                    $scope.template_tree_data = treedata;
                    TemplateService.getDraftsByOid(applicationPeriod.oid, oids).then(function(response) {
                        treedata = TemplateTreeService.addDraftsToTree(treedata, response.data, oidToRowMap, $scope.template_tree_control);
                        TemplateService.getDefaultTemplates().then(function(response) {
                            treedata = TemplateTreeService.addTemplatesToTree(treedata, response.data, oidToRowMap, $scope.template_tree_control)
                            $scope.template_tree_data = treedata;
                            $scope.templatesUpdated = true;
                            $scope.showTreeLoadingIndicator=false;

                        });
                     });
                } else if ($scope.hakukohdeTabActive) {
                    $scope.draft_tree_data = treedata;
                    TemplateTreeService.getHakukohteetByApplicationPeriod(applicationPeriod.oid).then(function(response){
                        var treeAndHakukohdeOids = TemplateTreeService.addHakukohteetToTree(treedata, response.data, oidToRowMap, $scope.draft_tree_control);
                        treedata = treeAndHakukohdeOids.tree;
                        var oids = treeAndHakukohdeOids.hakukohdeoids;
                        var oidMap = treeAndHakukohdeOids.hakukohdeOidMap;
                        TemplateService.getDraftsByTags(oids).then(function(response) {
                            treedata = TemplateTreeService.addDraftsToTree(treedata, response.data, oidMap, $scope.draft_tree_control);
                            $scope.draft_tree_data = treedata;
                            $scope.draftsUpdated = true;

                            $scope.showTreeLoadingIndicator=false;
                        });
                    });
                }
            });
        };

        TemplateService.getApplicationTargets().then(function(data) {
            var list = _.chain(data)
                .map(function(item) {
                    var name = TemplateService.getNameFromHaku(item);
                    item.name = name;
                    return item;
                })
                .value();
            $scope.letterTypes[1].list = list;
            $scope.applicationPeriodList = list;
        });



        $scope.editDraft = function(draft) {
            $state.go('letter-templates_draft_edit',
                {'templatename': draft.templateName,
                    'language' : draft.language,
                    'applicationPeriod': draft.applicationPeriod,
                    'orgoid' : draft.organizationOid,
                    'fetchTarget': draft.fetchTarget});
        };
        $scope.publishDraft = function(draft) {
        };
        $scope.removeDraft = function(draft) {
        };

        $scope.createDraft = function(draft) {
            var modalInstance = $modal.open({
                templateUrl: 'views/letter-templates/views/partials/newDraft.html',
                controller: 'DraftCreationDialog',
                size: 'lg',
                resolve: {
                    applicationPeriod: function() {return $scope.selectedApplicationPeriod;},
                    hakukohde: function() {return draft;}
                }
            });
        };

    }]);

angular.module('letter-templates').controller('DraftEditDialog',
    ['$scope', '$modalInstance', 'applicationPeriod', 'hakukohde', 'TemplateService', '$state',
        function ($scope, $modalInstance, applicationPeriod, hakukohde, TemplateService, $state) {

            $scope.languages = [
                {value: 'FI', text: 'suomi'},
                {value: 'SV', text: 'ruotsi'},
                {value: 'EN', text: 'englanti'}
            ];
            $scope.applicationTypes = [
                {value: 'koekutsukirje', text: 'Koekutsukirje'},
                {value: 'hyvaksymiskirje', text: 'Hyväksymiskirje'},
                {value: 'jalkiohjauskirje', text: 'Jälkiohjauskirje'}];

            $scope.languageSelection = "FI";

            $scope.baseTemplates = [];
            TemplateService.getBaseTemplates().success(function(data) {
                $scope.baseTemplates = data;
            });

            $scope.template = TemplateService.getTemplate();
            $scope.baseTemplate = TemplateService.getBase();
            $scope.selectedApplicationPeriod = applicationPeriod;
            $scope.hakukohde = hakukohde;
            $scope.hakukohdeNimi = TemplateService.getNameFromHaku(hakukohde, '');
            $scope.templateSelection = {};

            $scope.cancel = function () {
                $modalInstance.dismiss('cancel');
            };

            $scope.selectBase = function(template) {
                $scope.templateSelection = template;
            };

            $scope.confirm = function() {
                $modalInstance.close();
                $state.go('letter-templates_draft_create',{
                    'templatename': $scope.templateSelection.type,
                    'language': $scope.languageSelection,
                    'orgoid': $scope.hakukohde.orgOid,
                    'applicationPeriod': $scope.selectedApplicationPeriod.oid,
                    'fetchTarget': $scope.hakukohde.fetchTarget
                });
            };
        }]);

angular.module('letter-templates').controller('DraftCreationDialog',
['$scope', '$modalInstance', 'applicationPeriod', 'hakukohde', 'TemplateService', '$state',
function ($scope, $modalInstance, applicationPeriod, hakukohde, TemplateService, $state) {

    $scope.languages = [
        {value: 'FI', text: 'suomi'},
        {value: 'SV', text: 'ruotsi'},
        {value: 'EN', text: 'englanti'}
    ];
    $scope.applicationTypes = [
        {value: 'koekutsukirje', text: 'Koekutsukirje'},
        {value: 'hyvaksymiskirje', text: 'Hyväksymiskirje'},
        {value: 'jalkiohjauskirje', text: 'Jälkiohjauskirje'}];

    $scope.languageSelection = "FI";

    $scope.baseTemplates = [];
    TemplateService.getBaseTemplates().success(function(data) {
        $scope.baseTemplates = data;
    });

    $scope.template = TemplateService.getTemplate();
    $scope.baseTemplate = TemplateService.getBase();
    $scope.selectedApplicationPeriod = applicationPeriod;
    $scope.hakukohde = hakukohde;
    $scope.hakukohdeNimi = TemplateService.getNameFromHaku(hakukohde, '');
    $scope.templateSelection = {};

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.selectBase = function(template) {
        $scope.templateSelection = template;
    };

    $scope.confirm = function() {
        $modalInstance.close();
        $state.go('letter-templates_draft_create',{
            'templatename': $scope.templateSelection.type,
            'language': $scope.languageSelection,
            'orgoid': $scope.hakukohde.orgOid,
            'applicationPeriod': $scope.selectedApplicationPeriod.oid,
            'fetchTarget': $scope.hakukohde.fetchTarget
        });
    };
}]);