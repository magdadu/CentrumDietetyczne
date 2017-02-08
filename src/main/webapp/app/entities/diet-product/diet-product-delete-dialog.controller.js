(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('Diet_productDeleteController',Diet_productDeleteController);

    Diet_productDeleteController.$inject = ['$uibModalInstance', 'entity', 'Diet_product'];

    function Diet_productDeleteController($uibModalInstance, entity, Diet_product) {
        var vm = this;

        vm.diet_product = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Diet_product.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
