(function () {
    'use strict';

    angular
        .module('inzynierkaApp')
        .factory('Client', Client);

    Client.$inject = ['$resource'];

    function Client ($resource) {
        var service = $resource('api/clients', {}, {
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
