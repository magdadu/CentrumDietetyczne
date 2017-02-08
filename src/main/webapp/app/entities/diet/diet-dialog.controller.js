(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietDialogController', DietDialogController);

    DietDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Diet','Diet_product'];

    function DietDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Diet,Diet_product) {
        var vm = this;

        vm.diet = entity;
        vm.clear = clear;
        vm.save = save;
         console.log( $stateParams);
         getDietInfo();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });


 function getDietInfo(){
    Diet_product.query(function(result) {
                            vm.diet_products = result;
                            //console.log(vm.diet_products);
                        });

                        }
        function clear () {
            $uibModalInstance.dismiss('cancel');
            console.log(vm);
        }

        function save () {
            vm.isSaving = true;
            if (vm.diet.id !== null) {
                Diet.update(vm.diet, onSaveSuccess, onSaveError);
            } else {
                Diet.save(vm.diet, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('inzynierkaApp:dietUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
