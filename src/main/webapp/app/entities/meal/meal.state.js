(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('meal', {
            parent: 'entity',
            url: '/meal',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.meal.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meal/meals.html',
                    controller: 'MealController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('meal');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('meal-detail', {
            parent: 'entity',
            url: '/meal/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.meal.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/meal/meal-detail.html',
                    controller: 'MealDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('meal');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Meal', function($stateParams, Meal) {
                    return Meal.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'meal',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('meal-detail.edit', {
            parent: 'meal-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meal/meal-dialog.html',
                    controller: 'MealDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meal', function(Meal) {
                            return Meal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meal.new', {
            parent: 'meal',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meal/meal-dialog.html',
                    controller: 'MealDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                amount: null,
                                unit: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('meal', null, { reload: 'meal' });
                }, function() {
                    $state.go('meal');
                });
            }]
        })
        .state('meal.edit', {
            parent: 'meal',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meal/meal-dialog.html',
                    controller: 'MealDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Meal', function(Meal) {
                            return Meal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meal', null, { reload: 'meal' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('meal.delete', {
            parent: 'meal',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/meal/meal-delete-dialog.html',
                    controller: 'MealDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Meal', function(Meal) {
                            return Meal.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('meal', null, { reload: 'meal' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
