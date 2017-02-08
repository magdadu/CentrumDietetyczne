(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('dietetician', {
            parent: 'entity',
            url: '/dietetician',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.dietetician.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dietetician/dieteticians.html',
                    controller: 'DieteticianController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dietetician');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('dietetician-detail', {
            parent: 'entity',
            url: '/dietetician/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.dietetician.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/dietetician/dietetician-detail.html',
                    controller: 'DieteticianDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dietetician');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Dietetician', function($stateParams, Dietetician) {
                    return Dietetician.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'dietetician',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('dietetician-detail.edit', {
            parent: 'dietetician-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dietetician/dietetician-dialog.html',
                    controller: 'DieteticianDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dietetician', function(Dietetician) {
                            return Dietetician.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dietetician.new', {
            parent: 'dietetician',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dietetician/dietetician-dialog.html',
                    controller: 'DieteticianDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                firstname: null,
                                lastname: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('dietetician', null, { reload: 'dietetician' });
                }, function() {
                    $state.go('dietetician');
                });
            }]
        })
        .state('dietetician.edit', {
            parent: 'dietetician',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dietetician/dietetician-dialog.html',
                    controller: 'DieteticianDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Dietetician', function(Dietetician) {
                            return Dietetician.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dietetician', null, { reload: 'dietetician' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('dietetician.delete', {
            parent: 'dietetician',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/dietetician/dietetician-delete-dialog.html',
                    controller: 'DieteticianDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Dietetician', function(Dietetician) {
                            return Dietetician.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('dietetician', null, { reload: 'dietetician' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
