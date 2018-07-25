angular.module('workflow', [
	'ui.bootstrap',
	'ui.router',
	'workflow.directives',
	'workflow.services',
	'workflow.templates'
]);
angular.module('workflow').constant('CONSTANTS', {
	'apiUrl': './api/v1'
});
angular.module('workflow').config(['$httpProvider', '$stateProvider', '$urlRouterProvider', function ($httpProvider, $stateProvider, $urlRouterProvider) {
	$stateProvider
		.state('workflow', {
			abstract: true,
			url: '/entities',
			resolve: {
				entities: ['$http', '$q', '$log', 'CONSTANTS', function($http, $q, $log, CONSTANTS) {
					$log.log('[Workflow] Fetch entities');
					var deferred = $q.defer();
					$http({
						method: 'GET',
						url: CONSTANTS.apiUrl + '/entities'
					}).then(function(response) {
						deferred.resolve(response.data);
					});
					return deferred.promise;
				}]
			},
			views: {
				'': {
					templateUrl: 'commons/layouts/default.tpl.html'
				}
			}
		})
		.state('workflow.entities', {
			url: '',
			views: {
				'sidebar': {
					templateUrl: 'entities/sidebar.tpl.html',
					controller: ['$scope', '$state', 'entities', function($scope, $state, entities) {
						angular.extend($scope, {
							entities: entities,
							selectedEntity: ''
						});
						$scope.$watch('selectedEntity', function(newValue, oldValue) {
							if($scope.selectedEntity != '')
								$state.go('workflow.entities.entity', { entity: $scope.selectedEntity.iwName })
						});
					}]
				}
			}
		})
		.state('workflow.entities.entity', {
			url: '/:entity',
			resolve: {
				items: ['Entity', '$stateParams', function(Entity, $stateParams) {
					return Entity.query({entity: $stateParams.entity})
				}],
				schema: ['$http', '$stateParams', '$q', '$log', 'CONSTANTS', function($http, $stateParams, $q, $log, CONSTANTS) {
					$log.log('[Workflow] Fetch schema');
					var deferred = $q.defer();
					$http({
						method: 'GET',
						url: CONSTANTS.apiUrl + '/' + $stateParams.entity + '/schema'
					}).then(function(response) {
						deferred.resolve(response.data);
					});
					return deferred.promise;
				}]
			},
			views: {
				'@workflow': {
					templateUrl: 'entities/index.tpl.html',
					controller: 'EntityController'
				}
			}
		})
		.state('workflow.entities.entity.add', {
			url: '/add',
			views: {
				'@workflow': {
					templateUrl: 'entities/form.tpl.html',
					controller: 'EntityAddController'
				}
			}
		})
		.state('workflow.entities.entity.edit', {
			url: '/:id/edit',
			resolve: {
				item: ['Entity', '$stateParams', function(Entity, $stateParams) {
					return Entity.get({entity: $stateParams.entity, id: $stateParams.id})
				}]
			},
			views: {
				'@workflow': {
					templateUrl: 'entities/form.tpl.html',
					controller: 'EntityEditController'
				}
			}
		});
	$urlRouterProvider.otherwise('/entities');
}]);
angular.module('workflow').run(['$log', function($log) {
	'use strict';
	$log.log('[Workflow] Initialisation');
}]);
angular.module('workflow').controller('ApplicationController', ['$rootScope', '$scope', '$log', function($rootScope, $scope, $log) {
	$log.log('[Workflow] ApplicationController');
}]);
angular.module('workflow').controller('EntitiesController', ['$rootScope', '$scope', '$log', 'entities', function($rootScope, $scope, $log, entities) {
	$log.log('[Workflow] EntitiesController');
	angular.extend($scope, {
		entities: entities
	});
}]);
angular.module('workflow').controller('EntityController', ['$rootScope', '$scope', '$state', '$log', 'Entity', 'items', 'schema', function($rootScope, $scope, $state, $log, Entity, items, schema) {
	$log.log('[Workflow] EntityController');
	angular.extend($scope, {
		items: items,
		schema: schema,
		type: 'view'
	});
	$scope.create = function() {
		$state.go('workflow.entities.entity.add', {entity: schema.name});
	};
	$scope.delete = function(entity) {
		if (confirm('Êtes-vous sure de vouloir supprimer cet enregistrement ?')) {
			Entity.delete({entity: schema.name, id: entity[$scope.schema.primaryKey]}).$promise.then(function(response) {
				$state.go('workflow.entities.entity', {entity: schema.name}, { reload: true, notify: true });
			});
		}
	}
}]);
angular.module('workflow').controller('EntityAddController', ['$rootScope', '$scope', '$state', '$log', 'Entity', 'schema', function($rootScope, $scope, $state, $log, Entity, schema) {
	angular.extend($scope, {
		schema: schema,
		data: {},
		type: 'edit'
	});
	$scope.save = function(entityForm) {
		Entity.save({entity: $scope.schema.name}, $scope.data).$promise.then(function(response) {
			$state.go('workflow.entities.entity', {entity: schema.name}, { reload: true, notify: true });
		});
	}
}]);
angular.module('workflow').controller('EntityEditController', ['$rootScope', '$scope', '$state', '$log', 'Entity', 'schema', 'item', function($rootScope, $scope, $state, $log, Entity, schema, item) {
	angular.extend($scope, {
		schema: schema,
		data: item,
		type: 'edit'
	});
	$scope.save = function() {
		Entity.save({entity: $scope.schema.name, id: $scope.data[$scope.schema.primaryKey]}, $scope.data).$promise.then(function(response) {
			$state.go('workflow.entities.entity', {entity: schema.name}, { reload: true, notify: true });
		});
	}
}]);
