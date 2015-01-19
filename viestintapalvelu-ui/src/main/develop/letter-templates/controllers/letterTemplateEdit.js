'use strict';

angular.module('letter-templates').controller('LetterTemplateEditCtrl',
    ['$scope', '$http', '$state', '$filter', '$modal', '$window', 'Global', 'PersonService', 'TemplateService',
    function($scope, $http, $state, $filter, $modal, $window, Global, PersonService, TemplateService) {

        $scope.editorOptions = Global.getEditorOptions();

        TemplateService.getTemplateByIdAndState($state.params.templateId, 'luonnos').success(function(result) {
            $scope.template = result;
            TemplateService.getStructureById($scope.template.structureId).success(function(structure) {
            $scope.contentReplacements = structure.replacements;
            });
            PersonService.getPerson(result.storingOid).success(function(person) {
            $scope.saverName = person.firstNames + " " + person.lastName;
            })
            if ($scope.template.applicationPeriods != null && $scope.template.applicationPeriods.length > 0) {
            TemplateService.getApplicationTargets().then(function(targets) {
                var target = $filter('filter')(targets, {oid: $scope.template.applicationPeriods[0]});
                var templateLanguage = $scope.template.language;
                $scope.applicationTargetForDisplay = templateLanguage === 'SV' ? target[0].nimi.kieli_sv : templateLanguage === 'EN' ? target[0].nimi.kieli_en : target[0].nimi.kieli_fi;
            })
            }
        }).error(function(result) {
            //TODO handle errors
        });
        
        $scope.getMatchingTemplateReplacement = function(key) {
            var found = $filter('filter')($scope.template.replacements, {name: key});
            return found.length ? found[0] : {name: key, defaultValue: ''};
        };
        
        $scope.save = function() {
            TemplateService.updateTemplate().put({}, $scope.template, function() {
            //TODO: some feedback to the user
            });
        };
        
        $scope.cancel = function() {
            $state.go('letter-templates.overview');
        };
        
        $scope.publish = function() {
            var modalInstance = $modal.open({
            templateUrl: 'views/letter-templates/views/partials/publishdialog.html',
            controller: 'PublishTemplate',
            size: 'sm',
            resolve: {
                templateId: function () {
                return $scope.template.id
                },
                state: function() {
                return $scope.template.state
                },
                template: function () {
                return $scope.template
                }
            }
            });

            modalInstance.result.then(function() {
            $state.go('letter-templates_overview');
            });
        };

        $scope.previewPDF = function(args) {
            var contents = "";
            //$scope.
            angular.forEach($scope.template.replacements,function(value,index){
                if (value.name == 'sisalto') {
                    contents = value.defaultValue;
                }
            });
            
            $http({
                url: '/viestintapalvelu/api/v1/preview/letterbatch/pdf',
                method: "POST",
                data: {'templateId' : $scope.template.id,
                       'templateState' : $scope.template.state,
                       'letterContent' : contents},
                headers: {
                   'Content-type': 'application/json'
                },
                responseType: 'arraybuffer'
            }).success(function (data, status, headers, config) {
                var blob = new Blob([data], {type: "application/pdf"});
                var objectUrl = URL.createObjectURL(blob);
                window.open(objectUrl);
            }).error(function (data, status, headers, config) {

            });
        };

        $scope.previewLetter = function(args) {
            $window.open("/viestintapalvelu/api/v1/preview/letterbatch/email?templateId="+$scope.template.id+"&templateState="+$scope.template.state);
        };

        $scope.buttons = [
                          {label: 'Peruuta', click: $scope.cancel, type: 'default'},
                          {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                          {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                          {label: 'Tallenna', click: $scope.save, primary: true},
                          {label: 'Julkaise', click: $scope.publish}];
}]);