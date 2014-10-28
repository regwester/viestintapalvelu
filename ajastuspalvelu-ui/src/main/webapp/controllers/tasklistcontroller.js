app.factory('ScheduledTasks', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/list", {}, {
        get : {
            method : "GET",
            isArray : true
        }
    });
});

app.controller('TaskListController', ['$scope', '$location', 'ScheduledTasks', function($scope, $location, ScheduledTasks) {
    ScheduledTasks.get({}, function(result) {
	$scope.tasks = result
    });
    
    $scope.create = function() {
	$location.path("/create")
    }
    
    $scope.edit = function(task) {
	$location.path("/edit/" + task)
    }
}]);