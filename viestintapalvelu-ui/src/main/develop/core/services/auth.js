angular.module('core.services').factory('RightsModel', ['$q', 'PersonService', function($q, PersonService) {
    var deferred = $q.defer();

    var factory = (function() {
        var instance = {};

        PersonService.getRights().success(function(result) {
            instance = result;
            deferred.resolve(instance);
        });

        return instance;
    })();

    return deferred.promise;
}]);

angular.module('core.services').factory('AuthService', ['$q', 'RightsModel', function($q, RightsModel) {

    var readAccess = function(model) {
	return model.rightToViewTemplates;
    };

    var crudDraftAccess = function(model, org) {
	return model.organizationOids.indexOf(org) > -1  && model.rightToEditDrafts;
    };
    
    var ophCrud = function(model) {
	return model.rightToEditTemplates && model.ophUser;
    };

    var accessCheck = function(accessFunction, orgOid) {
	var deferred = $q.defer();

        RightsModel.then(function(model){
            if(accessFunction(model, orgOid)) {
                deferred.resolve();
            } else {
                deferred.reject();
            }
        });

        return deferred.promise;
    };

    return {
        read : function() {
            return accessCheck(readAccess);
        },

        crudDraftOrg : function(orgOid) {
            return accessCheck(crudDraftAccess, orgOid);
        },

        crudOph : function() {
            return accessCheck(ophCrud);
        }
        
    };
}]);