'use strict';

angular.module('letter-templates').controller('LetterTemplateEditCtrl', ['$scope', '$state', 'Global', 'TemplateService', function($scope, $state, Global, TemplateService) {
	
	$scope.editorOptions = Global.getEditorOptions();

        TemplateService.getTemplateByIdAndState($state.params.templateId, 'luonnos').success(function(result) {
            $scope.template = result;
        }).error(function(result) {
            //TODO handle errors
        });
        
        $scope.buttons = [
                          {label: 'Peruuta', click: $scope.cancel, type: 'default'},
                          {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                          {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                          {label: 'Tallenna', click: $scope.save, primary: true},
                          {label: 'Julkaise', click: $scope.publish}];
}]);