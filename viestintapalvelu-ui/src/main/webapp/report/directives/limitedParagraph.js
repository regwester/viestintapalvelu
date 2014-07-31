angular.module('report').directive('limitedParagraph', function() {
	
  return {
    restrict: 'E',
    replace: true,
    template: '<p class="limited-paragraph">{{ content | limitTo : getLimit() | htmlToPlainText }}<span ng-if="isLimited"><span ng-if="!showAll">...</span> <a ng-click="toggleShowAll()">{{ getToggleButtonText() }}</a></span></p>',
    scope: {
      'content': '=',
      'showButtonText': '@',
      'hideButtonText': '@',
      'limit': '='
    },
    link: function(scope) {
      scope.showAll = false; // hide extra text initially
      scope.isLimited = scope.content.length > scope.limit;

      scope.getLimit= function() {
        if (scope.isLimited && !scope.showAll) {
          return scope.limit;
        } else {
          return scope.content.length;
        }
      };

      scope.toggleShowAll = function() {
        scope.showAll = !scope.showAll;
      };

      scope.getToggleButtonText = function() {
        return scope.showAll ? scope.hideButtonText : scope.showButtonText;
      };

    }
  };

});
