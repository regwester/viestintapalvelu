'use strict';

angular.module('viestintapalvelu')
.directive('upload', ['uploadManager',
  function factory(uploadManager) {
    return {
      restrict: 'A',
      link: function (scope, element, attrs) {
        $(element).fileupload({
          dataType: 'json',
          add: function (e, data) {
            uploadManager.add(data);
          },
          progressall: function (e, data) {
            var progress = parseInt(data.loaded / data.total * 100, 10);
            uploadManager.setProgress(progress);
          },
          done: function (e, data) {
            uploadManager.setResult(data.result);
            uploadManager.setProgress(0);
          }
        });
      }
   };
  }
]);