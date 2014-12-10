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
            {"Organisaatio ja kirjetyyppi":"Aalto-yliopisto", lang: "", status:"", children: [
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Suomi", status:"Opetushallituksen oletuskirjepohja"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Ruotsi", status:"Opetushallituksen oletuskirjepohja"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Englanti", status:"Opetushallituksen oletuskirjepohja"},
                {"Organisaatio ja kirjetyyppi":"Insinööritieteiden korkeakoulu", lang: "", status:"", children: [
                    {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                    {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                    {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                    {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                    {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                    {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                    {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Suomi", status:"Opetushallituksen oletuskirjepohja"},
                    {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Ruotsi", status:"Opetushallituksen oletuskirjepohja"},
                    {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Englanti", status:"Opetushallituksen oletuskirjepohja"}
                ]}
            ]},
            {"Organisaatio ja kirjetyyppi":"Helsingin Yliopisto", lang: "", status:"", children: [
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Suomi", status:"Opetushallituksen oletuskirjepohja"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Ruotsi", status:"Opetushallituksen oletuskirjepohja"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Englanti", status:"Opetushallituksen oletuskirjepohja"}
            ]}
        ];

        $scope.draft_tree_data = [
            {"Organisaatio ja kirjetyyppi":"Aalto-yliopisto", lang: "", status:"", children: [
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje (luonnos)", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje (luonnos)", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje (luonnos)", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
            ]},
            {"Organisaatio ja kirjetyyppi":"Helsingin Yliopisto", lang: "", status:"", children: [
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje (luonnos)", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje (luonnos)", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje (luonnos)", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
            ]}
        ];

        TemplateService.getApplicationTargets().then(function(data) {
            $scope.applicationPeriodList = data;
        });
}]);