(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietDeleteController',DietDeleteController);

    DietDeleteController.$inject = ['$uibModalInstance', 'entity', 'Diet'];

    function DietDeleteController($uibModalInstance, entity, Diet) {
        var vm = this;

        vm.diet = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Diet.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
