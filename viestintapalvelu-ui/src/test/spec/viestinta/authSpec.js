describe('Authentication Tests', function() {

    var compile, scope, timeout, q;
    
    var rightToEditTemplates = false;

    var dummyPersonService = {
	    getRights : function() {
		var rights = {"organizationOids":["1.2.246.562.10.00000000001","1.2.246.562.10.53814745062"],"ophUser":true,"rightToViewTemplates":true,"rightToEditTemplates":rightToEditTemplates,"rightToEditDrafts":true}
		deferred = q.defer();
		deferred.resolve(rights);
		deferred.promise.success = function(fn) {
		    fn(rights);
		    return deferred.promise;
		};
		return deferred.promise;
		   
	    } 
    }

    beforeEach(function() {
	module('core.services');

	module(function ($provide) {
	    $provide.value('PersonService', dummyPersonService);
	});

	angular.mock.inject(function ($injector) {
	    mockPersonService = $injector.get('PersonService');
	})

	inject(function($compile, $rootScope, $timeout, $q) {
	    compile = $compile;
	    scope = $rootScope;
	    timeout = $timeout;
	    q = $q;
	});
    });

    createElementAndCompile = function() {
	var element = angular.element('<button auth="crudOph">Button</button>');
	compile(element)(scope);
	scope.$digest();
	return element
    }

    it("hides element when authentication fails", function () {
	var element = createElementAndCompile();
	timeout.flush();
	expect(element.hasClass('ng-hide')).toBe(true);
    });

    it("shows element when authentication is successful", function() {
	rightToEditTemplates = true;
	var element = createElementAndCompile();
	timeout.flush();
	expect(element.hasClass('ng-hide')).toBe(false);
    });
});