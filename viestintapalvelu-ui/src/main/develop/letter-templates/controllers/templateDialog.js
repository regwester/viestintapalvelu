'use strict';

angular.module('letter-templates')
    .controller('TemplateDialogCtrl', ['$scope', '$modalInstance', 'TemplateService', '$state', '_',
        function($scope, $modalInstance, TemplateService, $state, _) {

            //Default values
            $scope.applicationTarget = TemplateService.getApplicationTarget();
            $scope.languageSelection = 'FI';

            /*
            TemplateService.getApplicationTargets().then(function(data) {
                var list = [];
                for(var i = 0, max = data.length; i < max; i++){
                    list.push({name: data[i].nimi.kieli_fi, value: data[i].oid});
                }
                $scope.applicationTargets = list;
            });
             */
            TemplateService.getApplicationTargets().then(function(data) {
                $scope.applicationTargets = _.map(data, function(elem) {
                    return {name: elem.nimi.kieli_fi, value: elem.oid};
                });
            });
            TemplateService.getBaseTemplates().success(function(data) {
                console.log(data);
                $scope.baseTemplates = _.map(data, function(elem) {
                    return {name: elem.name, value: elem.oid};
                });
            }).error(function(error) {
                //TODO: log or display
            });
            $scope.languages = [
                {value: 'FI', text: 'suomi'},
                {value: 'SV', text: 'ruotsi'},
                {value: 'EN', text: 'englanti'}
            ];
            $scope.applicationTypes = [
                {value: 'admission', text: 'Koekutsukirje'},
                {value: 'acceptance', text: 'Hyväksymiskirje'},
                {value: 'rejection', text: 'Jälkiohjauskirje'}];

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