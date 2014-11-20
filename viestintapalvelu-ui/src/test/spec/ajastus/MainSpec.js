describe("Main app test", function() {
    
    it('Maps routes to controllers', function() {
	module('ajastuspalvelu');
	
	inject(function($route) {
	    expect($route.routes['/etusivu'].controller).toBe('TaskListController')
	    expect($route.routes['/create'].controller).toBe('CreateTaskController')
	    expect($route.routes['/edit/:task'].controller).toBe('EditTaskController')
	    
	    expect($route.routes['/etusivu'].templateUrl).toEqual('partials/frontpage.html')
	    expect($route.routes['/create'].templateUrl).toEqual('partials/createtask.html')
	    expect($route.routes['/edit/:task'].templateUrl).toEqual('partials/edittask.html')

	    expect($route.routes[null].redirectTo).toEqual('/etusivu')
	});
    });
})

