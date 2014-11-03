'use strict';

app.factory('Hakus', function($resource) {
    return $resource(SERVICE_REST_PATH + "options/hakus", {}, {
        get : {
            method : "GET",
            isArray : true
        }
    });
});

app.factory('Tasks', function($resource) {
    return $resource(SERVICE_REST_PATH + "task", {}, {
        get : {
            method : "GET",
            isArray : true
        }
    });
});