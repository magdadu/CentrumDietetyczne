(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietAppointmentController', DietAppointmentController);

    DietAppointmentController.$inject = ['$scope', '$state', 'DietAppointment'];

    function DietAppointmentController ($scope, $state, DietAppointment) {
        var vm = this;

        vm.dietAppointments = [];

        loadAll();

        function loadAll() {
            DietAppointment.query(function(result) {
                vm.dietAppointments = result;
            });
        }
    }
})();
