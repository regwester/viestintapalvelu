'use strict';

angular.module('letter-templates')
    .controller('LetterTemplateCreateCtrl', ['$scope', 'Global',
        function($scope, Global) {

            $scope.editorOptions = Global.getEditorOptions();

            $scope.letter = {subject: 'Testi', body: ''};

            $scope.cancel = function(){
                console.log('Cancelled');
            }
            $scope.saveLetter = function(){
                console.log('Saved');
            }
            $scope.previewLetter = function(){
                console.log('Preview');
            }
        }
    ]);