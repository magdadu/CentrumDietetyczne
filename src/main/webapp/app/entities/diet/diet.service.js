(function() {
    'use strict';
    angular
        .module('inzynierkaApp')
        .factory('Diet', Diet);

    Diet.$inject = ['$resource'];

    function Diet ($resource) {
        var resourceUrl =  'api/diets/:id';

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
