'user strict';

angular.module('letter-templates').controller('EditDraftCtrl', ['$scope','$state','$filter','Global','TemplateService','TemplateTreeService', function($scope, $state, $filter, Global, TemplateService, TemplateTreeService) {
    
    $scope.editorOptions = Global.getEditorOptions();
    
    var stateParams = $state.params;
    
    $scope.targetIsOrg = stateParams.fetchTarget ? false : true;
    
    TemplateService.getDraft(stateParams.templatename, stateParams.language, stateParams.applicationPeriod, stateParams.fetchTarget, stateParams.orgoid).success(function(result) {
        $scope.draft = result;
        TemplateService.getApplicationTargets().then(function(periods) {
            var period = $filter('filter')(periods, {oid: $scope.draft.applicationPeriod});
            var templateLanguage = $scope.draft.languageCode;
            $scope.applicationPeriodForDisplay = templateLanguage === 'SV' ? period[0].nimi.kieli_sv : templateLanguage === 'EN' ? period[0].nimi.kieli_en : period[0].nimi.kieli_fi;
        });
        
        if ($scope.targetIsOrg) {
            TemplateTreeService.getOrganizationName($scope.draft.organizationOid, $scope.draft.languageCode).then(function(orgName) {
                $scope.organizationForDisplay = orgName.data;                
            });
        }
    }).error(function(err) {
        console.log(err);
    });
    

    
    $scope.buttons = [{label: 'Peruuta', click: $scope.cancel, type: 'default'},
                      {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                      {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                      {label: 'Tallenna', click: $scope.save, primary: true}];
}]);