'user strict';

angular.module('letter-templates').controller('EditDraftCtrl', ['$scope','$state','$filter','Global','TemplateService','TemplateTreeService', 'PreviewService', 
                                                                function($scope, $state, $filter, Global, TemplateService, TemplateTreeService, PreviewService) {
    
    $scope.titleText = "Muokkaa kirjeen luonnosta";
    
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
    
    TemplateService.getTemplateByNameStateApplicationPeriodAndLanguage(stateParams.templatename, stateParams.language, stateParams.applicationPeriod, '').then(function(result) {
        $scope.template = result.data;
        angular.forEach(result.data.replacements, function(value) {
            if (value.name === 'otsikko') {
                $scope.titleTemplate = value.defaultValue;                
            }
        })
    });
    
    $scope.save = function() {
        var draft = $scope.draft;
        var draftUpdateObj = {id: draft.draftId.toString(), content: draft.replacements['sisalto'], orgoid: draft.organizationOid};
        TemplateService.updateDraft().put({}, draftUpdateObj, function() {
            //TODO: some feedback to the user
        });
    }
    
    $scope.cancel = function() {
        $state.go('letter-templates.overview');
    };
    
    $scope.previewPDF = function() {
        PreviewService.previewPDF($scope.template.id, $scope.template.state, $scope.draft.replacements['sisalto']);
    };
    
    $scope.previewLetter = function() {
        PreviewService.previewLetter($scope.template.id, $scope.template.state, $scope.draft.replacements['sisalto']);
    };
    
    $scope.buttons = [{label: $filter('i18n')('common.btn.cancel'), click: $scope.cancel, type: 'default'},
                      {label: $filter('i18n')('template.btn.preview.pdf'), click: $scope.previewPDF, type: 'default'},
                      {label: $filter('i18n')('template.btn.preview.email'), click: $scope.previewLetter, type: 'default'},
                      {label: $filter('i18n')('common.btn.save'), click: $scope.save, primary: true}];
}]);