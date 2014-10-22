describe("Main app test", function() {
    
    it('Maps routes to controllers', function() {
	module('ajastuspalvelu');
	
	inject(function($route) {
	    expect($route.routes['/etusivu'].controller).toBe('TaskListController');
	    //expect($route.routes['/phones'].templateUrl).toEqual('partials/frontpage.html')
	    
	    expect($route.routes[null].redirectTo).toEqual('/etusivu')
	});
    });
})

