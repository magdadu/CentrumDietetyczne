(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('MealController', MealController);

    MealController.$inject = ['$scope', '$state', 'Meal'];

    function MealController ($scope, $state, Meal) {
        var vm = this;

        vm.meals = [];

        loadAll();

        function loadAll() {
            Meal.query(function(result) {
                vm.meals = result;
            });
        }
    }
})();
