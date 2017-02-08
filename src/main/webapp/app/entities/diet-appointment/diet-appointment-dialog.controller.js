(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietAppointmentDialogController', DietAppointmentDialogController);

    DietAppointmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DietAppointment', 'Client', 'Dietetician'];

    function DietAppointmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DietAppointment, Client, Dietetician) {
        var vm = this;

        vm.dietAppointment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.clients = Client.query();
        vm.dieteticians = Dietetician.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dietAppointment.id !== null) {
                DietAppointment.update(vm.dietAppointment, onSaveSuccess, onSaveError);
            } else {
                DietAppointment.save(vm.dietAppointment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('inzynierkaApp:dietAppointmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
