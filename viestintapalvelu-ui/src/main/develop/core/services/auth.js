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

    var readAccess = function(org, model) {
	return model.organizationOids.indexOf(org) > -1 && model.rightToViewTemplates;
    };

    var crudDraftAccess = function(org, model) {
	return model.organizationOids.indexOf(org) > -1  && model.rightToEditDrafts;
    };

    var accessCheck = function(orgOid, accessFunction) {
	var deferred = $q.defer();

        RightsModel.then(function(model){
            if(accessFunction(orgOid, model)) {
                deferred.resolve();
            } else {
                deferred.reject();
            }
        });

        return deferred.promise;
    };

    var ophRead = function(model) {
        return model.rightToViewTemplates && model.ophUser;

    };

    var ophCrud = function(model) {
	return model.rightToEditTemplates && model.ophUser;
    };

    var ophAccessCheck = function(accessFunction) {
        var deferred = $q.defer();

        RightsModel.then(function(model) {
            if(accessFunction(model)) {
                deferred.resolve();
            } else {
                deferred.reject();
            }
        });

        return deferred.promise;
    };

    return {
        readOrg : function(orgOid) {
            return accessCheck(orgOid, readAccess);
        },

        crudDraftOrg : function(orgOid) {
            return accessCheck(orgOid, crudDraftAccess);
        },

        readOph : function() {
            return ophAccessCheck(ophRead);
        },

        crudOph : function() {
            return ophAccessCheck(ophCrud);
        }
        
    };
}]);