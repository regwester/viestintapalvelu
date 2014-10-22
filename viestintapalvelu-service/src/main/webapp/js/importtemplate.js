angular.module('app').controller('TemplateController', ['$scope', '$http', '$window', 'Template', function ($scope, $http, $window, Template) {
    var templateUrl = "api/v1/template/";

    Template.getExamples().success(function (data) {
        $scope.templates = data;
    });

    $scope.templateChanged = function () {
        $scope.templateData = $scope.template.content;
        $scope.showTemplateData = true;
    };


    // get template  
    $scope.import = function () {
        var url = templateUrl + "get?";

        // split files if any
        if ($scope.files != null)
            $scope.filenames = $scope.files.split(',');
        else {
            alert("Tiedostojen nimet eivät voi olla tyhjiä.");
            return;
        }

        // set default if not defined
        if ($scope.language == null)
            $scope.language = "FI";

        // create url
        // add template
        url += "templateFile=" + $scope.filenames + "&";

        //replacements
        url += "replacementFile=" + $scope.replacementFile + "&";
        // add style
        if ($scope.styleFile) {
            url += "styleFile=" + $scope.styleFile + "&";
        }

        //add a name for the template
        url += "templateName=" + $scope.templateName + "&";

        // add language
        url += "lang=" + $scope.language;

        // get template
        getTemplate(url);
    }

    // get template
    function getTemplate(url) {
        $http.get(url).
            success(function (data) {
                console.dir(data);
                $scope.templateData = JSON.stringify(data);
                $scope.showTemplateData = true;
            }).
            error(function (data) {
                $window.alert("Tuonti epäonnistui");
            })
    }

    // store function
    $scope.store = function () {
        var url = templateUrl + "store";

        console.dir($scope.templateData);

        // store template
        storeTemplate(url, $scope.templateData);
    }

    // store template
    function storeTemplate(url, params) {
        $http.post(url, params).
            success(function (data) {
                console.dir(data);
                $window.alert("Tallentaminen onnistui");
                $scope.templateData = null;
                $scope.showTemplateData = false;
                $scope.files = null;
                $scope.styleFile = null;
                $scope.language = null;
            }).
            error(function (data) {
                $window.alert("Tallentaminen epäonnistui");
            })
    }
}]);
