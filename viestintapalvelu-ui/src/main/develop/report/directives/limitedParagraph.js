angular.module('report').directive('limitedParagraph',[function() {
  return {
    restrict: 'E',
    replace: true,
    template: '<div class="limited-paragraph">' +
                '<div class="limited-content" ng-bind-html="content | trustAsHtml"/>' +
                '<span ng-if="isLimited">' +
                  '<a ng-click="toggleShowAll()">{{ getToggleButtonText() }}</a>' +
                '</span>' +
              '</div>',
    scope: {
      'content': '=',
      'showButtonText': '@',
      'hideButtonText': '@',
      'limit': '='
    },
    link: function(scope, elem, attrs) {
      scope.showAll = false; // hide extra text initially
      scope.isLimited = scope.content.length > scope.limit;
      scope.contentDiv = elem.find('div');
      if(scope.isLimited) {
        scope.contentDiv.css('height', scope.limit)
      }

      scope.toggleShowAll = function() {
        scope.showAll ? scope.contentDiv.css('height', scope.limit) : scope.contentDiv.css('height', 'auto');
        scope.showAll = !scope.showAll;
      };

      scope.getToggleButtonText = function() {
        return scope.showAll ? scope.hideButtonText : scope.showButtonText;
      };

    }
  };
}]);
