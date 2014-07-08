'use strict';

angular.module('report')
.service('SharedVariables', function() {
    var searchArgumentValue = '';
    var selectedOrganization = '';
    
    return {
        getSearchArgumentValue: function() {
            return searchArgumentValue;
        },
        setSearchArgumentValue: function(value) {
            searchArgumentValue = value;
        },
        getSelectedOrganizationValue: function() {
            return selectedOrganization;
        },
        setSelectedOrganizationValue: function(value) {
            selectedOrganization = value;
        }
    };
});
