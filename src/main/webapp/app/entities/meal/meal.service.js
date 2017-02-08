(function() {
    'use strict';
    angular
        .module('inzynierkaApp')
        .factory('Meal', Meal);

    Meal.$inject = ['$resource'];

    function Meal ($resource) {
        var resourceUrl =  'api/meals/:id';

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
