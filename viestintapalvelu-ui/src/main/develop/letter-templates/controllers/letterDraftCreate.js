'user strict';

angular.module('letter-templates').controller('CreateDraftCtrl', ['$scope','$state','$filter','$http','Global','TemplateService','TemplateTreeService', function($scope, $state, $filter, $http, Global, TemplateService, TemplateTreeService) {
    
    $scope.titleText = "Luo uusi kirjeen luonnos";
    
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
    
    TemplateService.getTemplateByNameStateApplicationPeriodAndLanguage(stateParams.templatename, stateParams.language, stateParams.applicationPeriod, '').then(function(result) {
        $scope.template = result.data;
        angular.forEach(result.data.replacements, function(value) {
            if (value.name === 'otsikko') {
                $scope.titleTemplate = value.defaultValue;                
            }
        })
    });
    
    $scope.draft = { replacements: {'sisalto':'<p></p>'}, templateName: stateParams.templatename, languageCode: templateLanguage, organizationOid: stateParams.orgoid, applicationPeriod: stateParams.applicationPeriod, fetchTarget: stateParams.fetchTarget};
        
    $scope.save = function() {
        TemplateService.insertDraft().post({}, $scope.draft, function() {
            $state.go('letter-templates_draft_edit', $state.params);
        });
    }
    
    $scope.cancel = function() {
        $state.go('letter-templates.overview');
    };
    
    $scope.previewPDF = function(args) {
        var content = $scope.draft.replacements['sisalto'];
        $http({
            url: '/viestintapalvelu/api/v1/preview/letterbatch/pdf',
            method: "POST",
            data: {'templateId' : $scope.template.id,
                   'templateState' : $scope.template.state,
                   'letterContent' : content},
            headers: {
               'Content-type': 'application/json'
            },
            responseType: 'arraybuffer'
        }).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "application/pdf"});
            var objectUrl = URL.createObjectURL(blob);
            window.open(objectUrl);
        }).error(function (data, status, headers, config) {

        });
    };

    $scope.previewLetter = function(args) {
        var content = $scope.draft.replacements['sisalto'];
        $http({
            url: '/viestintapalvelu/api/v1/preview/letterbatch/email',
            method: "POST",
            data: {'templateId' : $scope.template.id,
                   'templateState' : $scope.template.state,
                   'letterContent' : content},
            headers: {
               'Content-type': 'application/json'
            },
            responseType: 'arraybuffer'
        }).success(function (data, status, headers, config) {
            var blob = new Blob([data], {type: "text/plain"});
            var objectUrl = URL.createObjectURL(blob);
            window.open(objectUrl);
        }).error(function (data, status, headers, config) {
        });
    };
    
    $scope.buttons = [{label: 'Peruuta', click: $scope.cancel, type: 'default'},
                      {label: 'Esikatsele kirje (PDF)', click: $scope.previewPDF, type: 'default'},
                      {label: 'Esikatsele sähköposti', click: $scope.previewLetter, type: 'default'},
                      {label: 'Tallenna', click: $scope.save, primary: true}];
}]);
