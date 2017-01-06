(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('diet', {
            parent: 'app',
            url: '/diet',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/diet/diet.html',
                    controller: 'DietController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('diet');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
