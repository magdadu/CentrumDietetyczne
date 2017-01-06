(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider.state('contact', {
            parent: 'app',
            url: '/contact',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'app/contact/contact.html',
                    controller: 'ContactController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('contact');
                    return $translate.refresh();
                }]
            }
        });
    }
})();
