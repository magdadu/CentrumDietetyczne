(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('Diet_productDialogController', Diet_productDialogController);

    Diet_productDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Diet_product', 'Diet'];

    function Diet_productDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Diet_product, Diet) {
        var vm = this;

        vm.diet_product = entity;
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
            if (vm.diet_product.id !== null) {
                Diet_product.update(vm.diet_product, onSaveSuccess, onSaveError);
            } else {
                Diet_product.save(vm.diet_product, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('inzynierkaApp:diet_productUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
