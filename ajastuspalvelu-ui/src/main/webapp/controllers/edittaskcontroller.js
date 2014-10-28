'use strict';

app.factory('EditScheduledTask', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask", {}, {
        put : {
            method : "PUT"
        }
    });
});
    
app.factory('RemoveScheduledTask', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/close/:scheduledtaskid", {
	scheduledtaskid : "@scheduledtaskid"
    }, {
        remove : {
            method : "PUT"
        }
    });
});

app.controller('EditTaskController', ['$scope', '$location', '$routeParams', 'EditScheduledTask', 'RemoveScheduledTask', 
                                      function($scope, $location, $routeParams, EditScheduledTask, RemoveScheduledTask) {
    $scope.task = $routeParams.task;
    
    $scope.save = function() {
	EditScheduledTask.put({}, $scope.task, function(result) {
	    $location.path("/etusivu/");
	});
    }
    
    $scope.remove = function() {
	RemoveScheduledTask.remove({ scheduledtaskid : $scope.task.id},{}, function(result) {
	    $location.path("/etusivu/");
	});
    }
}]);