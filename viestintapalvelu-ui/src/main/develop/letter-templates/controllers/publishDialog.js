'use strict';

angular.module('letter-templates').controller('PublishTemplate', ['$scope', '$modalInstance', 'TemplateService', 'templateId', 'state', 'template', function ($scope, $modalInstance, TemplateService, templateId, state, template) {
    
    $scope.templateIdToPublish = templateId;
    $scope.state = state;
    $scope.template = template;
    $scope.modalData = {};
    
    $scope.cancel = function () {
	$modalInstance.dismiss('cancel');
    };

    $scope.publish = function () {
	var publishTemplate = function(template) {
	    template.state = 'julkaistu';
	    template.usedAsDefault = $scope.modalData.usedAsDefault;
	    TemplateService.updateTemplate().put({}, template, function () {
		$modalInstance.close();
	    });
	};
	if ($scope.template) {
	    publishTemplate(template);
	} else {
	    TemplateService.getTemplateByIdAndState($scope.templateIdToPublish, $scope.state).then(function(result) {
		publishTemplate(result.data);
	    });
	}
    };

}]);