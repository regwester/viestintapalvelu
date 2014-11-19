app.factory('ScheduledTasks', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/list", {}, {
        get : {
            method : "GET",
            isArray : true
        }
    });
});

app.factory('RemoveScheduledTask', ['$resource', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/close/:scheduledtaskid", {
	scheduledtaskid : "@scheduledtaskid"
    }, {
        remove : {
            method : "PUT"
        }
    });
}]);

app.controller('TaskListController', ['$scope', '$location', '$filter', '$modal', 'ScheduledTasks', 'Hakus', 'HakuNameByLocale', 
                                      function($scope, $location, $filter, $modal, ScheduledTasks, Hakus, HakuNameByLocale) {
    Hakus.get({}, function(result) {
	$scope.hakus = result
	
	ScheduledTasks.get({}, function(result) {
	    $scope.tasks = result
	    angular.forEach($scope.tasks, function(task) {
		task.hakuName = $scope.hakuByName(task.hakuOid)
	    });
	});
    });
    
    $scope.create = function() {
	$location.path("/create")
    }
    
    $scope.edit = function(task) {
	$location.path("/edit/" + task)
    }
    
    $scope.hakuByName = function(hakuOid) {
	var haku = $filter('filter') ($scope.hakus, function (haku) {return haku.oid === hakuOid})[0]
	return HakuNameByLocale(haku)
    }
    
    $scope.deleteTask = function(taskId) {

	var modalInstance = $modal.open({
	    templateUrl: 'deleteTaskModal.html',
	    controller: 'DeleteTaskModal',
	    size: 'sm',
	    resolve: {
		taskId: function() {
		    return taskId
		}
	    }
	});

	modalInstance.result.then(function (taskId) {
	    $scope.tasks = $filter('filter')($scope.tasks, function(task) { return task.id != taskId});
	});
    }
    
}]);

app.controller('DeleteTaskModal', ['$scope', '$modalInstance', 'RemoveScheduledTask', 'taskId', function ($scope, $modalInstance, RemoveScheduledTask, taskId) {
    
    $scope.taskIdToDelete = taskId
    
    $scope.cancel = function () {
      $modalInstance.dismiss('cancel')
    };
    
    $scope.remove = function() {
	RemoveScheduledTask.remove({ scheduledtaskid : $scope.taskIdToDelete },{}, function(result) {
	    $modalInstance.close($scope.taskIdToDelete)
	});
    }
}]);