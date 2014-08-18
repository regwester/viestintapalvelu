angular.module('email').directive('rowTable', ['$filter', function($filter) {

  function link(scope, element, attrs) {

    scope.currentPage = 1;
    scope.totalItems = function() {
      return scope.items.length;
    };

    scope.selectItem = function(id) {
      scope.onselect({id: id})
    }

    //Temporary workaround until angular ui tooltip supports templates
    scope.getAttachmentsAsHtml = function(item) {
      var attachments = item.attachInfo;
      var html = '';
      for(var i = 0; i < attachments.length; i++) {
        html += '<span>' + attachments[i].fileName + '</span>';
        html += '&nbsp;';
        html += '<span>(' + $filter('bytesToSize')(attachments[i].fileSize) + ')</span>';
        html += '<br/>';
      }
      return html;
    };
  }

  function Controller($scope) {
    function groupItems() {
      var items = [];
      for (var i = 0; i < $scope.items.length; i++) {
        if (i % $scope.limit === 0) {
          items[Math.floor(i / $scope.limit)] = [$scope.items[i]];
        } else {
          items[Math.floor(i / $scope.limit)].push($scope.items[i]);
        }
      }
      return items;
    }

    $scope.currentItems = function() {
      return groupItems()[$scope.currentPage - 1];
    }
  }

  return {
    restrict: 'E',
    replace: true,
    templateUrl: './email/views/partials/rowTable.html',
    scope: {
      'items': '=',
      'empty': '@',
      'ondelete': '&',
      'onselect': '&',
      'limit': '='
    },
    link: link,
    controller: Controller
  };
}]);
