'use strict';

angular.module('viestintapalvelu')
.filter('bytesToSize', function() {
  return function(bytes) {
    var s = ['Bytes', 'KB', 'MB', 'GB', 'TB']; //localize these?
    if(bytes == 0) {
      return 'n/a';
    }
    var e = Math.floor(Math.log(bytes) / Math.log(1024));
    return (bytes / Math.pow(1024, e)).toFixed() + " " + s[e];
    
  };
});