(function () {
    'use strict';

    angular
        .module('inzynierkaApp')
        .factory('Creatediet', Creatediet);

    Creatediet.$inject = ['$resource'];

    function  Creatediet ($resource) {
        var service = $resource('api/ creatediets', {}, {
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
        });

        return service;
    }
})();
