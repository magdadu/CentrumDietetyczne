(function () {
    'use strict';

    angular
        .module('inzynierkaApp')
        .factory('DietAppointment', DietAppointment);

    DietAppointment.$inject = ['$resource'];

    function DietAppointment ($resource) {
        var service = $resource('api/diet-appointments', {}, {
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
