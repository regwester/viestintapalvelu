'use strict';

angular.module('letter-templates')
    .controller('TemplateController', ['$scope', '$state', 'TemplateService', 'TemplateTreeService', function($scope, $state, TemplateService, TemplateTreeService) {

        $scope.applicationPeriodList = [];
        $scope.selectedApplicationPeriod = "Select one";
        $scope.col_defs = [
            { field: "lang", displayName: "Kieli"},
            { field: "status", displayName: "Tila"}
        ];

        $scope.user_oid = '';

        $scope.placeholder_valitse_haku = "Valitse haku";

        $scope.my_tree_handler = function(branch){
            console.log(branch);
            if(branch["isLetter"]) {
                //TODO handle letter opening here
            }
        }

        $scope.updateTreeData = function(applicationPeriod) {

            var query = TemplateTreeService.getOrganizationHierarchy(applicationPeriod.oid);
            query.then(function(response){
                var parsedTree = TemplateTreeService.getParsedTreeGrid(response);
                var treedata = parsedTree.tree;
                var organizationOIDS = parsedTree.oids;
                var oidToRowMap = parsedTree.oidToRowMap
                $scope.template_tree_data = treedata;
                TemplateService.getTemplatesByOid(organizationOIDS).then(function(response) {
                    treedata = TemplateTreeService.addTemplatesToTree(treedata, response.data, oidToRowMap);
                    $scope.template_tree_data = treedata;
                });
            });
        }

        $scope.template_tree_data = [
            {"Organisaatio ja kirjetyyppi":"", lang: "", status:"", children:[]}
        ];

        $scope.draft_tree_data = [
            {"Organisaatio ja kirjetyyppi":"", lang: "", status:"", children:[]}
        ];

        TemplateService.getApplicationTargets().then(function(data) {
            $scope.applicationPeriodList = data;
        });
}]);