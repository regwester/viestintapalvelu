/*
 * Copyright (c) 2014 The Finnish National Board of Education - Opetushallitus
 *
 * This program is free software: Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * European Union Public Licence for more details.
 */

angular.module('app').controller('ApplicationPeriodsController', ['$scope', 'Template', function($scope, Template) {
    $scope.templates = [];
    $scope.versions = [];
    $scope.contents = "";
    $scope.template = null;
    $scope.version = null;
    $scope.applicationPeriods = [];
    $scope.usedAsDefault = false;

    Template.getNames().success(function (data) {
        $scope.templates = data;
    });

    $scope.templateChanged = function() {
        $scope.version = null;
        $scope.applicationPeriods = [];
        Template.listVersionsByName($scope.template, true, true).success(function (data) {
            $scope.versions = data;
            if (!$scope.applicationPeriods || !$scope.applicationPeriods.length) {
                $scope.applicationPeriods = [{oid:"", deleted:false}];
            }
        });
    };

    $scope.versionSelected = function() {
        $scope.applicationPeriods = createPeriods($scope.version);
        $scope.usedAsDefault = $scope.version.usedAsDefault;
    };

    $scope.add = function() {
        if ($scope.version) {
            $scope.applicationPeriods.push({oid:"", deleted:false});
        }
    };

    function createPeriods(version) {
        var periods = [];
        angular.forEach(version.applicationPeriods, function (v) {
            periods.push({oid: v, deleted:false});
        });
        if (!periods.length) {
            periods.push({oid: "", deleted:false});
        }
        return periods;
    };

    function extractPeriods(applicationPeriods) {
        var periods = [];
        angular.forEach(applicationPeriods, function (v) {
            if (!v.deleted) {
                periods.push(v.oid);
            }
        });
        return periods;
    };

    $scope.save = function() {
        if ($scope.version) {
            Template.saveAttachedApplicationPeriods($scope.version.id, extractPeriods($scope.applicationPeriods),
                            $scope.usedAsDefault)
                    .success(function() {
                alert("Liitokset tallennettu.");
            }).failure(function() {
                alert("Tallennus epäonnistui");
            });
        }
    };

    $scope.delete = function(applicationPeriod) {
        applicationPeriod.deleted = true;
    };

    $scope.versionDescription = function(version) {
        return version.timestamp + (version.description != null ? "("+version.description+")":"");
    };

}]);