/*
 * Copyright (c) 2014 The Finnish Board of Education - Opetushallitus
 *
 * This program is free software:  Licensed under the EUPL, Version 1.1 or - as
 * soon as they will be approved by the European Commission - subsequent versions
 * of the EUPL (the "Licence");
 *
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at: http://www.osor.eu/eupl/
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * European Union Public Licence for more details.
 */

'user strict';

angular.module('letter-templates').controller('CreateDraftCtrl', ['$scope','$state','$filter','Global','TemplateService','TemplateTreeService', 'PreviewService', 
                                                                  function($scope, $state, $filter, Global, TemplateService, TemplateTreeService, PreviewService) {
    
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
    };
    
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
