(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietAppointmentDeleteController',DietAppointmentDeleteController);

    DietAppointmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'DietAppointment'];

    function DietAppointmentDeleteController($uibModalInstance, entity, DietAppointment) {
        var vm = this;

        vm.dietAppointment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DietAppointment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
