app.factory('CreateScheduledTask', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask", {}, {
        post : {
            method : "POST"
        }
    });
});

app.controller('CreateTaskController', ['$scope', '$location', '$filter', 'CreateScheduledTask', 'Hakus', 'Tasks', 
                                        function($scope, $location, $filter, CreateScheduledTask, Hakus, Tasks) {
    
    Hakus.get({}, function(result) {
	$scope.hakus = result
	$scope.selectedHaku = result[0]
    });

    Tasks.get({}, function(result) {
	$scope.tasks = result
	$scope.selectedTask = result[0]
    });
    
    $scope.selectedDate = $filter('date')(new Date(), 'dd-MM-yyyy');
    $scope.selectedTime = new Date();
    
    $scope.create = function() {
	var dateSelected = $filter('date')($scope.selectedDate, 'yyyy-dd-MM')
	dateSelected += "T" + $filter('date')($scope.selectedTime, 'hh:mm:ss.sss')
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