'use strict';

reportingApp.service('SharedVariables', function() {
    var searchArgumentValue = '';
    
    return {
        getSearchArgumentValue: function() {
            return searchArgumentValue;
        },
        setSearchArgumentValue: function(value) {
        	searchArgumentValue = value;
        }
    };
});
