'user strict';

angular.module('letter-templates').controller('EditDraftCtrl', ['$scope','$state','$filter','Global','TemplateService','TemplateTreeService', function($scope, $state, $filter, Global, TemplateService, TemplateTreeService) {
    
    $scope.editorOptions = Global.getEditorOptions();
    
    var stateParams = $state.params;
    
    $scope.targetIsOrg = stateParams.fetchTarget ? false : true;
    
    TemplateService.getDraft(stateParams.templatename, stateParams.language, stateParams.applicationPeriod, stateParams.fetchTarget, stateParams.orgoid).success(function(result) {
        $scope.draft = result;
        var templateLanguage = $scope.draft.languageCode;
        TemplateService.getApplicationTargets().then(function(periods) {
            var period = $filter('filter')(periods, {oid: $scope.draft.applicationPeriod});
            $scope.applicationPeriodForDisplay = templateLanguage === 'SV' ? period[0].nimi.kieli_sv : templateLanguage === 'EN' ? period[0].nimi.kieli_en : period[0].nimi.kieli_fi;
        });
        
        if ($scope.targetIsOrg) {
            TemplateTreeService.getOrganizationName($scope.draft.organizationOid, $scope.draft.languageCode).then(function(orgName) {
                $scope.organizationForDisplay = orgName.data;                
            });
        } else {
            TemplateService.getFetchTargetsByOid($scope.draft.fetchTarget).then(function(target) {
               var targetNames = target.data.result.hakukohteenNimet; 
               $scope.fetchTargetForDisplay = templateLanguage === 'SV' ? targetNames['kieli_sv'] : templateLanguage === 'EN' ? targetNames['kieli_en'] : targetNames['kieli_fi'];
            });
        }
    }).error(function(err) {
        console.log(err);
    });
    
    $scope.save = function() {
        var draft = $scope.draft;
        var draftUpdateObj = {id: draft.draftId.toString(), content: draft.replacements['sisalto'], orgoid: draft.organizationOid};
        TemplateService.updateDraft().put({}, draftUpdateObj, function() {
            //TODO: some feedback to the user
        });
    }
    
    $scope.buttons = [{label: 'Peruuta', click: $scope.cancel, type: 'default'},
                      {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                      {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                      {label: 'Tallenna', click: $scope.save, primary: true}];
}]);