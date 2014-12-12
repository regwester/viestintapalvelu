angular.module('core.filters').filter('capitalize', function() {
    return function(input) {
	var capitalizeFirstLetter = function(text) {
	    return text.charAt(0).toUpperCase() + text.substr(1);
	}
	return (!!input) ? input.replace(/([^\W_]+[^\s-]*) */g, capitalizeFirstLetter(input)) : '';
    };
});
