(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('Diet_productController', Diet_productController);

    Diet_productController.$inject = ['$scope', '$state', 'Diet_product'];

    function Diet_productController ($scope, $state, Diet_product) {
        var vm = this;

        vm.diet_products = [];

        loadAll();

        function loadAll() {
            Diet_product.query(function(result) {
                vm.diet_products = result;
            });
        }
    }
})();
