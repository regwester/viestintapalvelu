app.factory('ScheduledTasks', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/list", {}, {
        get : {
            method : "GET",
            isArray : true
        }
    });
});

app.controller('TaskListController', ['$scope', '$location', '$filter', 'ScheduledTasks', 'Hakus', 
                                      function($scope, $location, $filter, ScheduledTasks, Hakus) {
    Hakus.get({}, function(result) {
	$scope.hakus = result
	
	ScheduledTasks.get({}, function(result) {
	    $scope.tasks = result
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
	var name = haku.nimi['kieli_fi']
	if (!name || name == '') name = haku.nimi['kieli_en']
	return name
    }
}]);