'use strict';

angular.module('letter-templates')
    .controller('TemplateDialogCtrl', ['$scope', '$modalInstance', 'TemplateService', '$state', '_',
        function($scope, $modalInstance, TemplateService, $state, _) {

            //Default values
            $scope.applicationTarget = TemplateService.getApplicationTarget();
            $scope.languageSelection = 'FI';

            TemplateService.getApplicationTargets().then(function(data) {
                $scope.applicationTargets = _.map(data, function(elem) {
                    return {name: elem.nimi.kieli_fi, value: elem.oid};
                });
            });
            TemplateService.getBaseTemplates().success(function(data) {
                $scope.baseTemplates = data;
            });

            $scope.languages = [
                {value: 'FI', text: 'suomi'},
                {value: 'SV', text: 'ruotsi'},
                {value: 'EN', text: 'englanti'}
            ];
            $scope.applicationTypes = [
                {value: 'koekutsukirje', text: 'Koekutsukirje'},
                {value: 'hyvaksymiskirje', text: 'Hyväksymiskirje'},
                {value: 'jalkiohjauskirje', text: 'Jälkiohjauskirje'}];

            $scope.selectBase = function(base) {
                TemplateService.setBase(base);
            };

            $scope.selectTarget = function(target) {
                TemplateService.setTarget(target);
            };

            $scope.selectType = function(type) {
                TemplateService.setType(type);
            };

            $scope.selectLanguage = function(language) {
                TemplateService.setLanguage(language);
            };

            $scope.confirm = function() {
                $modalInstance.close();
                $state.go('letter-templates.create');
            };

            $scope.cancel = function () {
                $modalInstance.dismiss();
            };
        }
    ]);