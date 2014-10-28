app.factory('CreateScheduledTask', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask", {}, {
        post : {
            method : "POST"
        }
    });
});

app.controller('CreateTaskController', ['$scope', '$location', 'CreateScheduledTask', function($scope, $location, CreateScheduledTask) {
    
    $scope.create = function() {
	var task = {
		taskName: $scope.selectedTask,
		hakuOid: $scope.selectedHaku.oid,
		runtimeForSingle: $scope.runtimeForSingle
	}
	CreateScheduledTask.post({}, task, function(result) {
	            $location.path("/etusivu/");
	});
    }
    ScheduledTasks.get({}, function(result) {
	$scope.tasks = result
    });
}]);