'use strict';

app.factory('EditScheduledTask', ['$resource', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask", {}, {
        put : {
            method : "PUT"
        }
    });
}]);
    
app.factory('RemoveScheduledTask', ['$resource', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/close/:scheduledtaskid", {
	scheduledtaskid : "@scheduledtaskid"
    }, {
        remove : {
            method : "PUT"
        }
    });
}]);

app.factory('FetchScheduledTask', ['$resource', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/:scheduledtaskid", {
	scheduledtaskid : "@scheduledtaskid"
    }, {
        get : {
            method : "GET"
        }
    });
}]);

app.controller('EditTaskController', ['$scope', '$location', '$routeParams', 'EditScheduledTask', 'RemoveScheduledTask', 'FetchScheduledTask', 
                                      function($scope, $location, $routeParams, EditScheduledTask, RemoveScheduledTask, FetchScheduledTask) {
    
    
    FetchScheduledTask.get( {scheduledtaskid : $routeParams.task} , {}, function(result) {
	$scope.task = result;
    });
    
    
    $scope.save = function() {
	EditScheduledTask.put({}, $scope.task, function(result) {
	    $location.path("/etusivu/");
	});
    }
    
    $scope.remove = function() {
	RemoveScheduledTask.remove({ scheduledtaskid : $scope.task.id },{}, function(result) {
	    $location.path("/etusivu/");
	});
    }
}]);