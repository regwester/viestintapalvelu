angular.module('app').controller('TemplateController', ['$scope', '$window', 'Template', function ($scope, $window, Template) {

    var fileNamesNotAvailable = function(e) {
        $window.alert("Tiedostojen hakeminen epäonnistui.");
    };

    var importFailed = function(e) {
        $window.alert("Tuonti epäonnistui.");
    };

    Template.getExampleFiles().success(function(data) {
        $scope.exampleFiles = data;
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

    $scope.selectExample = function() {
        Template.getExample($scope.exampleFile)
            .success(function(data) {
                $scope.template = JSON.stringify(data);
            }).error(importFailed);
        $scope.showTemplateData = true;
    };

}]);
