app.factory('CreateScheduledTask', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask", {}, {
        post : {
            method : "POST"
        }
    });
});

app.controller('CreateTaskController', ['$scope', '$location', '$filter', 'CreateScheduledTask', 'Hakus', 'Tasks', 'HakuNameByLocale', 
                                        function($scope, $location, $filter, CreateScheduledTask, Hakus, Tasks, HakuNameByLocale) {
    
    Hakus.get({}, function(result) {
	$scope.hakus = result
	$scope.selectedHaku = result[0]
    });

    Tasks.get({}, function(result) {
	$scope.tasks = result
	$scope.selectedTask = result[0]
    });
    
    $scope.minDate = new Date()
    $scope.selectedDate = $filter('date')(new Date(), 'dd-MM-yyyy');
    $scope.selectedTime = new Date();
    
    $scope.create = function() {
	var dateSelected = $filter('date')($scope.selectedDate, 'yyyy-MM-dd')
	dateSelected += "T" + $filter('date')($scope.selectedTime, 'HH:mm:ss.sss')
	var task = {
		taskId: $scope.selectedTask.id,
		hakuOid: $scope.selectedHaku.oid,
		runtimeForSingle: dateSelected
	}
	CreateScheduledTask.post({}, task, function(result) {
	    $scope.back()
	});
    }
    
    $scope.openCalendar = function($event) {
	$event.preventDefault();
	$event.stopPropagation();
	$scope.opened = true;
    };
    
    $scope.hakuByName = function(haku) {
	return HakuNameByLocale(haku)
    }
    
    $scope.back = function() {
	$location.path("/etusivu/");
    }
    
}]);