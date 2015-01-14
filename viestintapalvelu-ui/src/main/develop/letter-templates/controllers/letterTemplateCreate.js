'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateCreateCtrl', ['$scope', 'Global', '$state', 'TemplateService', '$filter',
        function($scope, Global, $state, TemplateService, $filter) {

            // Default values
            $scope.editorOptions = Global.getEditorOptions();
            $scope.template = TemplateService.getTemplate();
            $scope.templateContent = TemplateService.getTemplateContent()
                .success(function(data) {
                    console.log(data);
                }).error(function(error) {
                    console.log(error);
                });

            /*
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
            */

            $scope.cancel = function() {
                $state.go('letter-templates_overview');
            };
            $scope.save = function() {
                TemplateService.saveTemplate($scope.template);
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
                {label: $filter('i18n')('common.btn.cancel'), click: $scope.cancel, type: 'default'},
                {label: $filter('i18n')('template.btn.preview.pdf'), click: $scope.previewPDF, type: 'default'},
                {label: $filter('i18n')('template.btn.preview.email'), click: $scope.previewLetter, type: 'default'},
                {label: $filter('i18n')('common.btn.save'), click: $scope.save, primary: true},
                {label: $filter('i18n')('common.btn.publish'), click: $scope.publish}];
        }
    ]);