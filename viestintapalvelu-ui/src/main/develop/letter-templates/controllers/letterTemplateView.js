'use strict';

angular.module('letter-templates').controller('LetterTemplateViewCtrl',
    ['$scope', '$state', '$filter', '$timeout', 'PersonService', 'TemplateService', 'PreviewService', 'AuthService',
     function($scope, $state, $filter, $timeout, PersonService, TemplateService, PreviewService, AuthService) {

        TemplateService.getTemplateByIdAndState($state.params.templateId, $state.params.state).success(function(result) {
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
        
        $scope.crudOph = false;
        
        $timeout(function() {
            AuthService.crudOph().then(function() { $scope.crudOph = true });
        }, 0);
        
        $scope.getMatchingTemplateReplacement = function(key) {
            var found = $filter('filter')($scope.template.replacements, {name: key});
            return found.length ? found[0] : {name: key, defaultValue: ''};
        };
        
        $scope.edit = function() {
            $state.go('letter-templates_edit', {'templateId': $scope.template.id});
        };
        
        $scope.isEditDisabled = function() {
            return $state.params.state !== 'luonnos' || !$scope.crudOph;
        }
        
        $scope.cancel = function() {
            $state.go('letter-templates.overview');
        };
        
        $scope.getContent = function() {
            var content = "";
            angular.forEach($scope.template.replacements,function(value,index){
                if (value.name == 'sisalto') {
                    content = value.defaultValue;
                }
            });
            return content;
        }
        
        $scope.previewPDF = function(args) {
            PreviewService.previewPDF($scope.template.id, $scope.template.state, $scope.getContent());
        };

        $scope.previewLetter = function(args) {
            PreviewService.previewLetter($scope.template.id, $scope.template.state, $scope.getContent());
        };

        $scope.buttons = [
                          {label: $filter('i18n')('common.btn.cancel'), click: $scope.cancel, type: 'default'},
                          {label: $filter('i18n')('template.btn.preview.pdf'), click: $scope.previewPDF, type: 'default'},
                          {label: $filter('i18n')('template.btn.preview.email'), click: $scope.previewLetter, type: 'default'},
                          {label: $filter('i18n')('common.btn.edit'), click: $scope.edit, disabled: $scope.isEditDisabled, primary: true}];
}]);
