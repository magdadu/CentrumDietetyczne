(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('MealDeleteController',MealDeleteController);

    MealDeleteController.$inject = ['$uibModalInstance', 'entity', 'Meal'];

    function MealDeleteController($uibModalInstance, entity, Meal) {
        var vm = this;

        vm.meal = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Meal.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
