(function () {
    'use strict';

    angular
        .module('inzynierkaApp')
        .factory('Message', Message);

    Message.$inject = ['$resource'];

    function Message ($resource) {
        var service = $resource('api/messages/:firstname', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'},
            'isRead':{ method: 'POST'}
        });

        return service;
    }
})();
