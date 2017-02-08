(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('dietAppointment', {
            parent: 'app',
            url: '/dietAppointment',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/dietAppointment/dietAppointment.html',
                    controller: 'dietAppointmentController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('dietAppointment');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
