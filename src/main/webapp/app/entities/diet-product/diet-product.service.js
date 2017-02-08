(function() {
    'use strict';
    angular
        .module('inzynierkaApp')
        .factory('Diet_product', Diet_product);

    Diet_product.$inject = ['$resource'];

    function Diet_product ($resource) {
        var resourceUrl =  'api/diet-products/:id';

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
