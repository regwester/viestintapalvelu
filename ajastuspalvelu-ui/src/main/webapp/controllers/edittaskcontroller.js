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

app.controller('EditTaskController', ['$scope', '$location', '$routeParams', '$filter', 'EditScheduledTask', 'RemoveScheduledTask', 'FetchScheduledTask', 'Hakus', 'Tasks', 'HakuNameByLocale', 
                                      function($scope, $location, $routeParams, $filter, EditScheduledTask, RemoveScheduledTask, FetchScheduledTask, Hakus, Tasks, HakuNameByLocale) {
    
    FetchScheduledTask.get( {scheduledtaskid : $routeParams.task} , {}, function(result) {
	$scope.task = result
	$scope.selectedDate = $scope.task.runtimeForSingle
	$scope.selectedTime = $scope.task.runtimeForSingle
	
	Hakus.get({}, function(result) {
		$scope.hakus = result
		$scope.selectedHaku = $filter('filter') (result, function (haku) {return haku.oid === $scope.task.hakuOid})[0]
	});
	
	Tasks.get({}, function(result) {
	    $scope.tasks = result
	    $scope.selectedTask = $filter('filter') (result, function (task) {return task.id === $scope.task.taskId})[0]
	});
    });
    
    $scope.save = function() {
	var taskToSave = $scope.task
	taskToSave.taskId = $scope.selectedTask.id
	taskToSave.hakuOid = $scope.selectedHaku.oid
	var dateSelected = $filter('date')($scope.selectedDate, 'yyyy-MM-dd')
	dateSelected += "T" + $filter('date')($scope.selectedTime, 'HH:mm:ss.sss')
	EditScheduledTask.put({}, taskToSave, function(result) {
	    $location.path("/etusivu/");
	});
    }
    
    $scope.remove = function() {
	RemoveScheduledTask.remove({ scheduledtaskid : $scope.task.id },{}, function(result) {
	    $location.path("/etusivu/");
	});
    }
    
    $scope.hakuByName = function(haku) {
	return HakuNameByLocale(haku)
    }
    
    $scope.openCalendar = function($event) {
	$event.preventDefault();
	$event.stopPropagation();
	$scope.opened = true;
    };
}]);