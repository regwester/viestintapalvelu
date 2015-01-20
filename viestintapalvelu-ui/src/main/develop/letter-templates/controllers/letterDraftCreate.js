'user strict';

angular.module('letter-templates').controller('CreateDraftCtrl', ['$scope','$state','$filter','Global','TemplateService','TemplateTreeService', function($scope, $state, $filter, Global, TemplateService, TemplateTreeService) {
    
    $scope.editorOptions = Global.getEditorOptions();
    
    var stateParams = $state.params;
    
    $scope.targetIsOrg = stateParams.fetchTarget ? false : true;

    var templateLanguage = stateParams.language;
    
    TemplateService.getApplicationTargets().then(function(periods) {
        var period = $filter('filter')(periods, {oid: stateParams.applicationPeriod});
        $scope.applicationPeriodForDisplay = templateLanguage === 'SV' ? period[0].nimi.kieli_sv : templateLanguage === 'EN' ? period[0].nimi.kieli_en : period[0].nimi.kieli_fi;
    });
    
    if ($scope.targetIsOrg) {
        TemplateTreeService.getOrganizationName(stateParams.orgoid, templateLanguage).then(function(orgName) {
            $scope.organizationForDisplay = orgName.data;                
        });
    } else {
        TemplateService.getFetchTargetsByOid(stateParams.fetchTarget).then(function(target) {
           var targetNames = target.data.result.hakukohteenNimet; 
           $scope.fetchTargetForDisplay = templateLanguage === 'SV' ? targetNames['kieli_sv'] : templateLanguage === 'EN' ? targetNames['kieli_en'] : targetNames['kieli_fi'];
        });
    }
    
    $scope.draft = { replacements: {'sisalto':'<p></p>'}, templateName: stateParams.templatename, languageCode: templateLanguage, organizationOid: stateParams.orgoid, applicationPeriod: stateParams.applicationPeriod, fetchTarget: stateParams.fetchTarget};
        
    $scope.save = function() {
        TemplateService.insertDraft().post({}, $scope.draft, function() {
            //TODO: some feedback to the user
        });
    }
    
    $scope.buttons = [{label: 'Peruuta', click: $scope.cancel, type: 'default'},
                      {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                      {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                      {label: 'Tallenna', click: $scope.save, primary: true}];
}]);
