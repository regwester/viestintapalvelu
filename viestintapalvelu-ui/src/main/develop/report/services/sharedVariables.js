'use strict';

angular.module('report')
.service('SharedVariables', function() {
    var searchArgumentValue = '';
    var selectedOrganization = '';
    var selectedPage = 1;
    
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
        },
        getSelectedPage: function() {
        	return selectedPage;
        },
        setSelectedPage: function(value) {
        	selectedPage = value;
        }
    };
});
