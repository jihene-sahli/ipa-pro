angular.module('workflow.directives', []);
angular.module('workflow.directives').filter('exclude', [function() {
	return function(input) {
		var out = [];
		for (var i = 0; i < input.length; i++) {
			/**
			 * Exclude primary key from fields
			 */
			if(input[i].primaryKey)
				continue;
			out.push(input[i]);
		}
		return out;
	}
}]);
angular.module('workflow.directives').filter('columns', ['$filter', function($filter) {
	return function(input, columns) {
		var out = [];
		var filteredColumns = columns ? columns.split(',') : [];
		for (var i = 0; i < input.length; i++) {
			if(filteredColumns.length > 0) {
				var found = $filter('filter')(filteredColumns, input[i].name, true);
				if(!found.length && !input[i].primaryKey)
					continue;
			}
			if(input[i].type == 'association' && input[i].association.type !== 'ManyToOne')
				continue;
			out.push(input[i]);
		}
		return out;
	}
}]);
angular.module('workflow.directives').directive('manyToOne', ['$http', '$compile', 'Entity', 'CONSTANTS', function($http, $compile, Entity, CONSTANTS) {
	return {
		restrict: 'AE',
		replace: true,
		templateUrl: 'commons/manyToOne.tpl.html',
		link: function($scope, $element, $attributes, model) {
			switch($scope.type) {
				default:
				case 'edit':
					$http({
						method: 'GET',
						url: CONSTANTS.apiUrl + '/' + $scope.field.association.className + '/schema'
					}).then(function(response) {
						$scope.field.association['schema'] = response.data;
						$scope.items = [];
						Entity.query({entity: $scope.field.association.className}).$promise.then(function(response) {
							/**
							 * TODO: In case of a self join, exclude the current item
							 */
							$scope.items = response;
						});
					});
					break;
				case 'view':
					break;
			}
		}
	}
}]);
