'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateCreateCtrl', ['$scope', 'Global',
        function($scope, Global) {

            $scope.editorOptions = Global.getEditorOptions();

            $scope.letter = {subject: 'Testi', body: ''};

            $scope.cancel = function(){
                console.log('Cancelled');
            };
            $scope.save = function(){
                console.log('Saved');
            };
            $scope.previewLetter = function(){
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