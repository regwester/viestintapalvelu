'use strict';

angular.module('letter-templates').controller('LetterTemplateEditCtrl', ['$scope', '$state', '$filter', 'Global', 'TemplateService', function($scope, $state, $filter, Global, TemplateService) {
	
	$scope.editorOptions = Global.getEditorOptions();

        TemplateService.getTemplateByIdAndState($state.params.templateId, 'luonnos').success(function(result) {
            $scope.template = result;
            TemplateService.getStructureById($scope.template.structureId).success(function(structure) {
        	$scope.contentReplacements = structure.replacements;
            });
        }).error(function(result) {
            //TODO handle errors
        });
        
        $scope.getMatchingTemplateReplacement = function(key) {
            var found = $filter('filter')($scope.template.replacements, {name: key});
            return found.length ? found[0] : {name: key, defaultValue: ''};
        }
        
        $scope.buttons = [
                          {label: 'Peruuta', click: $scope.cancel, type: 'default'},
                          {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                          {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                          {label: 'Tallenna', click: $scope.save, primary: true},
                          {label: 'Julkaise', click: $scope.publish}];
}]);