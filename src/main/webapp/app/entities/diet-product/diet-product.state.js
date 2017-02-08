(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('diet-product', {
            parent: 'entity',
            url: '/diet-product',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.diet_product.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diet-product/diet-products.html',
                    controller: 'Diet_productController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('diet_product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('diet-product-detail', {
            parent: 'entity',
            url: '/diet-product/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.diet_product.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diet-product/diet-product-detail.html',
                    controller: 'Diet_productDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('diet_product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Diet_product', function($stateParams, Diet_product) {
                    return Diet_product.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'diet-product',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('diet-product-detail.edit', {
            parent: 'diet-product-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-product/diet-product-dialog.html',
                    controller: 'Diet_productDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diet_product', function(Diet_product) {
                            return Diet_product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diet-product.new', {
            parent: 'diet-product',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-product/diet-product-dialog.html',
                    controller: 'Diet_productDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                isRecommended: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('diet-product', null, { reload: 'diet-product' });
                }, function() {
                    $state.go('diet-product');
                });
            }]
        })
        .state('diet-product.edit', {
            parent: 'diet-product',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-product/diet-product-dialog.html',
                    controller: 'Diet_productDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Diet_product', function(Diet_product) {
                            return Diet_product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diet-product', null, { reload: 'diet-product' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diet-product.delete', {
            parent: 'diet-product',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-product/diet-product-delete-dialog.html',
                    controller: 'Diet_productDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Diet_product', function(Diet_product) {
                            return Diet_product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diet-product', null, { reload: 'diet-product' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
