'use strict';

/*
* The limited file will be made of the beginning, the ellipsis, the ending and the file extension, if it exists
* e.g. invoking the filter with a filename "example_long_file_name_with_extension.txt" and a limit of 15
* would return example...sion.txt
*/
angular.module('core.filters')
  .filter('limitSizeWithMiddleEllipsis', function() {
    return function(filename, limit) {
      //regex for getting the file extension
      //returns the extension or unknown
      var re = /(?:\.([^.]+))?$/;
      var subLength;

      var length = filename.length;
      if(length > limit) {
        var extension = re.exec(filename)[1];
        if(extension && extension.length < limit) {
          //calculate the length of the beginning and ending parts
          subLength = Math.floor((limit - (extension.length + 1))/2);
          //subtract the extension + dot from the length
          length = length - (extension.length + 1);
          return filename.substring(0, subLength) + '...' + filename.substring(length - subLength, length) + '.' + extension;
        } else {
          subLength = Math.floor(limit/2);
          return filename.substring(0, subLength) + '...' + filename.substring(length - subLength);
        }
      }
      return filename;

    };
  });
