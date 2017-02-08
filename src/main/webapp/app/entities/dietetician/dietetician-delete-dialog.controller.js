(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DieteticianDeleteController',DieteticianDeleteController);

    DieteticianDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dietetician'];

    function DieteticianDeleteController($uibModalInstance, entity, Dietetician) {
        var vm = this;

        vm.dietetician = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dietetician.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
