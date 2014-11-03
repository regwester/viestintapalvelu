app.factory('CreateScheduledTask', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask", {}, {
        post : {
            method : "POST"
        }
    });
});

app.controller('CreateTaskController', ['$scope', '$location', 'CreateScheduledTask', 'Hakus', 'Tasks', 
                                        function($scope, $location, CreateScheduledTask, Hakus, Tasks) {
    
    Hakus.get({}, function(result) {
	$scope.hakus = result
	$scope.selectedHaku = result[0]
    });

    Tasks.get({}, function(result) {
	$scope.tasks = result
	$scope.selectedTask = result[0]
    });
    
    $scope.selectedDate = new Date();
    $scope.selectedTime = new Date();
    
    $scope.create = function() {
	var dateSelected = $scope.selectedDate
	dateSelected.setHours($scope.selectedTime.getHours)
	dateSelected.setMinutes($scope.selectedTime.getMinutes)
	var task = {
		taskId: $scope.selectedTask.id,
		hakuOid: $scope.selectedHaku.oid,
		runtimeForSingle: dateSelected
	}
	CreateScheduledTask.post({}, task, function(result) {
	            $location.path("/etusivu/");
	});
    }
    
    $scope.openCalendar = function($event) {
	$event.preventDefault();
	$event.stopPropagation();
	$scope.opened = true;
    };
    
    $scope.hakuByName = function(haku) {
	var name = haku.nimi['kieli_fi']
	if (!name || name == '') name = haku.nimi['kieli_en']
	return name
    }
    
}]);