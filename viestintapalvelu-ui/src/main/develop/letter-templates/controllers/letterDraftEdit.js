'user strict'

angular.module('letter-templates').controller('EditDraftCtrl', ['$scope','$state', 'TemplateService', function($scope, $state, TemplateService) {
    
    var stateParams = $state.params;
    
    $scope.templateName = stateParams.templateName;
    
    $scope.language = stateParams.language;
    
    //TemplateService.getDraft($state.)
    
    
    $scope.buttons = [{label: 'Peruuta', click: $scope.cancel, type: 'default'},
                      {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                      {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                      {label: 'Tallenna', click: $scope.save, primary: true}];
}]);