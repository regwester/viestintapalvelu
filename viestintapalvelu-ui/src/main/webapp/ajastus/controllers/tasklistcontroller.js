ajastusApp.factory('ScheduledTasks', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/list", {}, {
        get : {
            method : "POST",
            isArray : false
        }
    });
});

ajastusApp.factory('RemoveScheduledTask', ['$resource', function($resource) {
    return $resource(SERVICE_REST_PATH + "scheduledtask/close/:scheduledtaskid", {
	scheduledtaskid : "@scheduledtaskid"
    }, {
        remove : {
            method : "PUT"
        }
    });
}]);

ajastusApp.controller('TaskListController', ['$scope', '$location', '$filter', '$modal', 'ScheduledTasks', 'Hakus', 'HakuNameByLocale',
    function ($scope, $location, $filter, $modal, ScheduledTasks, Hakus, HakuNameByLocale) {
        $scope.reverse = true;
        $scope.selectedColumn = "runtime";
        $scope.index = 0;
        $scope.perPage = 10;
        $scope.count = 0;
        $scope.pages = [];
        $scope.previousNavigatable = false;
        $scope.nextNavigatable = false;
        $scope.loading = true;

        var _orderBy = {
            "taskName": "TASK_NAME",
            "hakuName": "APPLICATION_PERIOD",
            "runtime": "SINGLE_RUNTIME"
        };
        var _buildCriteria = function() {
            return {
                "index": $scope.index,
                "maxResultCount": $scope.perPage,
                "orderDirection": $scope.reverse ? "DESC":"ASC",
                "orderBy": _orderBy[$scope.selectedColumn]
            };
        };
        var _buildNavigation = function() {
            var pages = [];
            var pagesTotal = Math.ceil($scope.count/$scope.perPage),
                currentPage = Math.floor($scope.index / $scope.perPage)+ 1,
                firstAndLastLimit = 3;

            if (currentPage > firstAndLastLimit+1) {
                for (var i = 1; i <= firstAndLastLimit; ++i) {
                    pages.push({
                       number: i,
                       clickable: true
                    });
                }
                if (firstAndLastLimit < currentPage-2) {
                    pages.push({
                        number: "..."
                    });
                }
                if (currentPage > firstAndLastLimit+1) {
                    pages.push({
                        number: currentPage-1,
                        clickable: true
                    });
                }
            } else if (currentPage > 1) {
                for (var i = 1; i < currentPage; ++i) {
                    pages.push({
                        number: i,
                        clickable: true
                    });
                }
            }
            pages.push({
                number: currentPage,
                active: true,
                clickable: false
            });
            if (pagesTotal-currentPage > firstAndLastLimit) {
                if (currentPage < pagesTotal-firstAndLastLimit) {
                    pages.push({
                        number: currentPage+1,
                        clickable: true
                    });
                }
                if (currentPage+2 < pagesTotal-firstAndLastLimit+1) {
                    pages.push({
                        number: "..."
                    });
                }
                for (var i = pagesTotal-firstAndLastLimit+1; i <= pagesTotal; ++i) {
                    pages.push({
                        number: i,
                        clickable: true
                    });
                }
            } else if (currentPage < pagesTotal) {
                for (var i = currentPage+1; i <= pagesTotal; ++i) {
                    pages.push({
                        number: i,
                        clickable: true
                    });
                }
            }
            $scope.pages = pages;
            $scope.previousNavigatable = $scope.index > 0;
            $scope.nextNavigatable = ($scope.index + $scope.perPage) < $scope.count;
        };
        $scope.gotoPage = function(page) {
            $scope.index = (page-1)*$scope.perPage;
            _update();
        };
        $scope.nextPage = function() {
            $scope.index = $scope.index+$scope.perPage;
            _update();
        };
        $scope.previousPage = function() {
            $scope.index = Math.max(0, $scope.index-$scope.perPage);
            _update();
        };

        var _update = function() {
            ScheduledTasks.get(_buildCriteria(), function (result) {
                $scope.tasks = result.results;
                $scope.count = result.count;
                if ($scope.count && !$scope.tasks.length) {
                    $scope.previousPage();
                }
                _buildNavigation();
                $scope.loading = false;
                angular.forEach($scope.tasks, function (task) {
                    task.hakuName = $scope.hakuByName(task.hakuOid)
                });
            });
        };

        Hakus.get({}, function (result) {
            $scope.hakus = result;
            _update();
        });

        $scope.create = function () {
            $location.path("/create")
        };

        $scope.edit = function (task) {
            $location.path("/edit/" + task)
        };

        $scope.sort = function(by) {
            if ($scope.selectedColumn == by) {
                $scope.reverse = !$scope.reverse;
            }
            $scope.selectedColumn = by;
            _update();
        }

        $scope.hakuByName = function (hakuOid) {
            var haku = $filter('filter')($scope.hakus, function (haku) {
                return haku.oid === hakuOid
            })[0];
            return HakuNameByLocale(haku)
        };

        $scope.deleteTask = function (taskId) {
            var modalInstance = $modal.open({
                templateUrl: 'deleteTaskModal.html',
                controller: 'DeleteTaskModal',
                size: 'sm',
                resolve: {
                    taskId: function () {
                        return taskId
                    }
                }
            });

            modalInstance.result.then(function (taskId) {
                _update();
            });
        };

        $scope.determineSortClass = function (column) {
            if ($scope.selectedColumn === column) {
                if ($scope.reverse) {
                    return 'sortedColumn-desc'
                }
                return 'sortedColumn-asc'
            }
        };
    }]);

ajastusApp.controller('DeleteTaskModal', ['$scope', '$modalInstance', 'RemoveScheduledTask', 'taskId',
            function ($scope, $modalInstance, RemoveScheduledTask, taskId) {
    $scope.taskIdToDelete = taskId;

    $scope.cancel = function () {
        $modalInstance.dismiss('cancel');
    };

    $scope.remove = function () {
        RemoveScheduledTask.remove({ scheduledtaskid: $scope.taskIdToDelete }, {}, function () {
            $modalInstance.close($scope.taskIdToDelete);
        });
    };

}]);