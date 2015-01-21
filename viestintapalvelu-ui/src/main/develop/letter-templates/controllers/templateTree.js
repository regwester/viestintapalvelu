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
    .controller('TemplateController', ['$scope', '$state', 'TemplateService', 'TemplateTreeService', function($scope, $state, TemplateService, TemplateTreeService) {

        $scope.templateTabActive = true;
        $scope.hakukohdeTabActive = false;

        $scope.templatesUpdated = false;
        $scope.draftsUpdated = false;
        var selectAppPeriod = "Select one";

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

        $scope.my_tree_handler = function(branch, event){
            console.log(branch);
        };

        $scope.updateTreeData = function(applicationPeriod) {

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
                        });
                    });
                }
            });
        };

        TemplateService.getApplicationTargets().then(function(data) {
            $scope.applicationPeriodList = data;
        });
}]);