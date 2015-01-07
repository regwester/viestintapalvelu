'use strict';

angular.module('letter-templates')
    .controller('TemplateController', ['$scope', '$state', 'TemplateService', 'TemplateTreeService', function($scope, $state, TemplateService, TemplateTreeService) {

        var templateTabActive = true;
        var draftTabActive = false;

        var templatesUpdated = false;
        var draftsUpdated = false;
        var selectAppPeriod = "Select one";

        $scope.template_tree_control = {};
        $scope.draft_tree_control = {};
        $scope.applicationPeriodList = [];
        $scope.selectedApplicationPeriod = selectAppPeriod;
        $scope.col_defs = [
            { field: "lang", displayName: "Kieli"},
            { field: "status", displayName: "Tila"}
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
            templateTabActive = true;
            draftTabActive = false;
            if(!templatesUpdated && $scope.selectedApplicationPeriod !== selectAppPeriod ) {
                $scope.updateTreeData($scope.selectedApplicationPeriod);
            }
        };

        $scope.draftTab = function() {
            templateTabActive = false;
            draftTabActive = true;
            if(!draftsUpdated && $scope.selectedApplicationPeriod !== selectAppPeriod) {
                $scope.updateTreeData($scope.selectedApplicationPeriod);

            }
        };

        $scope.my_tree_handler = function(branch, event){
        };

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

        TemplateService.getApplicationTargets().then(function(data) {
            $scope.applicationPeriodList = data;
        });
}]);