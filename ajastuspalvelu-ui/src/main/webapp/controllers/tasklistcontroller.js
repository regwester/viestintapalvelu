app.factory('ScheduledTasks', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/list", {}, {
        get : {
            method : "GET",
            isArray : true
        }
    });
});

app.controller('TaskListController', ['$scope', '$location', '$filter', 'ScheduledTasks', 'Hakus', 'HakuNameByLocale', 
                                      function($scope, $location, $filter, ScheduledTasks, Hakus, HakuNameByLocale) {
    Hakus.get({}, function(result) {
	$scope.hakus = result
	
	ScheduledTasks.get({}, function(result) {
	    $scope.tasks = result
	    angular.forEach($scope.tasks, function(task) {
		task.hakuName = $scope.hakuByName(task.hakuOid)
	    });
	});
    });
    
    $scope.create = function() {
	$location.path("/create")
    }
    
    $scope.edit = function(task) {
	$location.path("/edit/" + task)
    }
    
    $scope.hakuByName = function(hakuOid) {
	var haku = $filter('filter') ($scope.hakus, function (haku) {return haku.oid === hakuOid})[0]
	return HakuNameByLocale(haku)
    }
    
}]);