/**
 * Created by Magda on 22.01.2017.
 */
(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('addResult', {
            parent: 'account',
            url: '/addResult',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/account/addResult/addResult.html',
                    controller: 'addResultController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('message');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
