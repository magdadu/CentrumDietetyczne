(function () {
    'use strict';

    angular
        .module('inzynierkaApp')
        .factory('Contact', Contact);

    Contact.$inject = ['$resource'];

    function Contact ($resource) {
        var service = $resource('api/contacts', {}, {
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
