angular.module('app').controller('TemplateController', ['$scope', '$window', 'Template', function ($scope, $window, Template) {
    $scope.selectedTemplates = [];
    $scope.selectedReplacements = [];

    var fileNamesNotAvailable = function(e) {
        $window.alert("Tiedostojen hakeminen epäonnistui.");
    };

    var importFailed = function(e) {
        $window.alert("Tuonti epäonnistui.");
    };

    Template.getExampleFiles().success(function(data) {
        $scope.exampleFiles = data;
    }).error(fileNamesNotAvailable);

    Template.getReplacementFiles().success(function(data) {
        $scope.replacementFiles = data;
    }).error(fileNamesNotAvailable);

    Template.getTemplateFiles().success(function(data) {
        $scope.templateFiles = data;
    }).error(fileNamesNotAvailable);

    Template.getStyleFiles().success(function(data) {
        $scope.styleFiles = data;
    }).error(fileNamesNotAvailable);

    $scope.saveTemplate = function() {
        Template.saveTemplate($scope.template).success(function(data) {
            console.dir(data);
            $window.alert("Tallentaminen onnistui.");
            $scope.showTemplateData = false;
        }).error(function (e) {
            $window.alert("Tallentaminen epäonnistui.");
        });
    };

    $scope.createTemplate = function() {
        var params = {};

        // Template can be created if at least one template base has been selected
        if ($scope.selectedTemplates.length > 0) {
            params.templateFiles = $scope.selectedTemplates.toString();
        } else {
            return;
        }

        params.replacementFile = $scope.replacementFile ? $scope.replacementFile : null;
        params.styleFile = $scope.styleFile ? $scope.styleFile : "ipost_pdf";
        params.templateName = $scope.templateName ? $scope.templateName : "mallikirjepohja";
        params.lang = $scope.language ? $scope.language : "FI";

        Template.createTemplate(params).success(function(template) {
            $scope.template = JSON.stringify(template);
            $scope.showTemplateData = true;
        }).error(function(e) {
            $window.alert("Pohjan luominen epäonnistui.");
        });
    };

    $scope.selectExample = function() {
        Template.getExample($scope.exampleFile)
            .success(function(data) {
                $scope.template = JSON.stringify(data);
            }).error(importFailed);
        $scope.showTemplateData = true;
    };

    $scope.selectTemplate = function() {
        $scope.selectedTemplates.push($scope.selectedTemplate);
    };

    $scope.removeTemplate = function(index) {
        $scope.selectedTemplates.splice(index, 1);
    };

    $scope.selectReplacement = function() {
        $scope.selectedReplacements.push($scope.selectedReplacement);
    };

    $scope.removeReplacement = function(index) {
        $scope.selectedReplacements.splice(index, 1);
    };

}]);
