(function() {
    'use strict';
    angular
        .module('inzynierkaApp')
        .factory('Dietetician', Dietetician);

    Dietetician.$inject = ['$resource'];

    function Dietetician ($resource) {
        var resourceUrl =  'api/dieteticians/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
