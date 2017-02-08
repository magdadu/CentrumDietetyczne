(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('message', {
            parent: 'app',
            url: '/message',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/message/message.html',
                    controller: 'MessageController',
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
