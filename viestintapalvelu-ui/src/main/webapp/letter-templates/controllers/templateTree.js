'use strict';

angular.module('letter-templates')
    .controller('TemplateController', ['$scope', '$state', 'templateService', function($scope, $state, templateService) {

        $scope.col_defs = [
            { field: "lang", displayName: "Kieli"},
            { field: "status", displayName: "Tila"}
        ];

        $scope.placeholder_valitse_haku = "Valitse haku";

        $scope.my_tree_handler = function(branch){
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

        $scope.test_tree_data2 = [
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


        $scope.init = function() {
        };


        $scope.updateTestData = function () {
            //var oid = "1.2.246.562.24.72453542949";
            //var templatesByOID = templateService.listByApplicationPeriod.get({applicationPeriod:oid});
            //console.log(templatesByOID);
            $scope.testData = Math.random();
        }
}]);