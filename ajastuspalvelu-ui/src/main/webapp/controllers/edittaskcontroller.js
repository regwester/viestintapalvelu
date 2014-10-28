app.factory('EditScheduledTask', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask", {}, {
        put : {
            method : "PUT"
        }
    });
});

app.controller('EditTaskController', ['$scope', '$location', '$routeParams', 'EditScheduledTask', 
                                      function($scope, $location, $routeParams, EditScheduledTask) {
    $scope.task = $routeParams.task
    
    $scope.save = function() {
	EditScheduledTask.put({}, $scope.task, function(result) {
	            $location.path("/etusivu/");
	});
    }
}]);