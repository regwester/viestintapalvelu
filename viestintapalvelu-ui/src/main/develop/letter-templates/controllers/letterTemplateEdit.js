'use strict';

angular.module('letter-templates').controller('LetterTemplateEditCtrl', ['$scope', '$state', '$filter', '$modal', 'Global', 'PersonService', 'TemplateService', 
                                                                         function($scope, $state, $filter, $modal, Global, PersonService, TemplateService) {
	
	$scope.editorOptions = Global.getEditorOptions();

        TemplateService.getTemplateByIdAndState($state.params.templateId, 'luonnos').success(function(result) {
            $scope.template = result;
            TemplateService.getStructureById($scope.template.structureId).success(function(structure) {
        	$scope.contentReplacements = structure.replacements;
            });
            PersonService.getPerson(result.storingOid).success(function(person) {
        	$scope.saverName = person.etunimet + " " + person.sukunimi;
        	console.log(person);
            })
        }).error(function(result) {
            //TODO handle errors
        });
        
        $scope.getMatchingTemplateReplacement = function(key) {
            var found = $filter('filter')($scope.template.replacements, {name: key});
            return found.length ? found[0] : {name: key, defaultValue: ''};
        }
        
        $scope.save = function() {
            TemplateService.updateTemplate().put({}, $scope.template, function() {
        	//TODO: some feedback to the user
            });
        }
        
        $scope.cancel = function() {
            $state.go('letter-templates_overview');
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
        
        $scope.buttons = [
                          {label: 'Peruuta', click: $scope.cancel, type: 'default'},
                          {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                          {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                          {label: 'Tallenna', click: $scope.save, primary: true},
                          {label: 'Julkaise', click: $scope.publish}];
}]);