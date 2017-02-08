(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietController', DietController);

    DietController.$inject = ['$scope', '$state', 'Diet','Diet_product'];

    function DietController ($scope, $state, Diet,Diet_product) {
        var vm = this;

        vm.diets = [];
        vm.diet_products=[];
        loadAll();

        function loadAll() {
            Diet.query(function(result) {
                vm.diets = result;
            });

        }
    }
})();
