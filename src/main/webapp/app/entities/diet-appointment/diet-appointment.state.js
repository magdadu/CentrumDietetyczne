(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('diet-appointment', {
            parent: 'entity',
            url: '/diet-appointment',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.dietAppointment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diet-appointment/diet-appointments.html',
                    controller: 'DietAppointmentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dietAppointment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('diet-appointment-detail', {
            parent: 'entity',
            url: '/diet-appointment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'inzynierkaApp.dietAppointment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/diet-appointment/diet-appointment-detail.html',
                    controller: 'DietAppointmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('dietAppointment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'DietAppointment', function($stateParams, DietAppointment) {
                    return DietAppointment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'diet-appointment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('diet-appointment-detail.edit', {
            parent: 'diet-appointment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-appointment/diet-appointment-dialog.html',
                    controller: 'DietAppointmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DietAppointment', function(DietAppointment) {
                            return DietAppointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diet-appointment.new', {
            parent: 'diet-appointment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-appointment/diet-appointment-dialog.html',
                    controller: 'DietAppointmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                hour: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('diet-appointment', null, { reload: 'diet-appointment' });
                }, function() {
                    $state.go('diet-appointment');
                });
            }]
        })
        .state('diet-appointment.edit', {
            parent: 'diet-appointment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-appointment/diet-appointment-dialog.html',
                    controller: 'DietAppointmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DietAppointment', function(DietAppointment) {
                            return DietAppointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diet-appointment', null, { reload: 'diet-appointment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('diet-appointment.delete', {
            parent: 'diet-appointment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/diet-appointment/diet-appointment-delete-dialog.html',
                    controller: 'DietAppointmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DietAppointment', function(DietAppointment) {
                            return DietAppointment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('diet-appointment', null, { reload: 'diet-appointment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
