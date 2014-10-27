app.factory('ScheduledTasks', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/list", {}, {
        get : {
            method : "GET",
            isArray : true
        }
    });
});

app.controller('TaskListController', ['$scope', 'ScheduledTasks', function($scope, ScheduledTasks) {
    ScheduledTasks.get({}, function(result) {
	$scope.tasks = result
    });
}]);