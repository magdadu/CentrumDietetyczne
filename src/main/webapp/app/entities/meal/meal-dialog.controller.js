(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('MealDialogController', MealDialogController);

    MealDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Meal', 'Diet'];

    function MealDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Meal, Diet) {
        var vm = this;

        vm.meal = entity;
        vm.clear = clear;
        vm.save = save;
        vm.diets = Diet.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.meal.id !== null) {
                Meal.update(vm.meal, onSaveSuccess, onSaveError);
            } else {
                Meal.save(vm.meal, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('inzynierkaApp:mealUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
