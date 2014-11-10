'use strict';

angular.module('letter-templates')
    .controller('TemplateController', ['$scope', '$state', 'templateService', function($scope, $state, templateService) {

        $scope.testData = 0.0;

        $scope.isCollapsed = false;

        $scope.col_defs = [
            { field: "lang", displayName: "Kieli"},
            { field: "status", displayName: "Tila"}
        ];


        $scope.my_tree_handler = function(branch){
            console.log('you clicked on', branch)
        }

        $scope.test_tree_data = [
            {"Organisaatio ja kirjetyyppi":"Aalto-yliopisto", lang: "", status:"", children: [
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Koekutsukirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Suomi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Ruotsi", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Hyväksymiskirje", lang: "Englanti", status:"Käytössä, muokattu 16.0.2014"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Suomi", status:"Opetushallituksen oletuskirjepohja"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Ruotsi", status:"Opetushallituksen oletuskirjepohja"},
                {"Organisaatio ja kirjetyyppi":"Jälkiohjauskirje", lang: "Englanti", status:"Opetushallituksen oletuskirjepohja"}
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

        $scope.foo= [
            {
                "id": 0,
                "name": "Melendez Waters"
            },
            {
                "id": 1,
                "name": "Alisa Brooks"
            },
            {
                "id": 2,
                "name": "Maynard Kramer"
            }
        ];


        $scope.init = function() {
            console.log("init");
            $scope.testData = "init data";
        };


        $scope.updateTestData = function () {
            //var oid = "1.2.246.562.24.72453542949";
            //var templatesByOID = templateService.listByApplicationPeriod.get({applicationPeriod:oid});
            //console.log(templatesByOID);
            $scope.testData = Math.random();
        }
}]);