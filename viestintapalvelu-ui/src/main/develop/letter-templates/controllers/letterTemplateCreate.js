'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateCreateCtrl', ['$scope', 'Global', '$state', 'templateService',
        function($scope, Global, $state, templateService) {

            $scope.editorOptions = Global.getEditorOptions();

            $scope.template = {};

            $scope.template.content = [
                {name: 'hyvaksymiskirje/liite', order: '1,2,3,4,5,6', content: '<html><p>order on turha</p></html>'}
            ];
            $scope.template.replacements = [
                {name: 'sisalto/liiteSisalto', defaultValue: '<html>replacement</html>', mandatory: 'true'}
            ];
            $scope.template.applicationPeriods = ['oidi1', 'oidi2', 'oidi3'];

            var name = '',
                language = '',
                description = '',
                styles = '',
                storingOid = '',
                organizationOid = '',
                usedAsDefault = "true/false",
                type = 'email/??',
                structureId = 12,
                structureName = '???',
                structure = {},
                state = 'luonnos/suljettu/julkaistu';

            $scope.cancel = function() {
                $state.go('letter-templates_overview');
            };
            $scope.save = function() {
                LetterService.saveTemplate($scope.template);
            };
            $scope.previewLetter = function() {
                console.log('Preview letter');
            };
            $scope.previewPDF = function() {
                console.log('Preview PDF');
            };
            $scope.publish = function() {
                console.log('Published');
            };

            $scope.buttons = [
                {label: 'Peruuta', click: $scope.cancel, type: 'default'},
                {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                {label: 'Tallenna', click: $scope.save, primary: true},
                {label: 'Julkaise', click: $scope.publish}];
        }
    ]);