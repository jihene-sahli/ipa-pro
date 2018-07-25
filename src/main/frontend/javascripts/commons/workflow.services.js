angular.module('workflow.services', [
	'ngResource'
]);
angular.module('workflow.services').factory('Entity', ['$resource', '$http', 'CONSTANTS', function($resource, $http, CONSTANTS) {
	var Entity = $resource(CONSTANTS.apiUrl + '/:entity/:id', {entity: '@entity', id: '@id'}, {
		query: {
			method: 'GET',
			isArray: true
		}
	});
	return Entity;
}]);
