(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietAppointmentDetailController', DietAppointmentDetailController);

    DietAppointmentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DietAppointment', 'Client', 'Dietetician'];

    function DietAppointmentDetailController($scope, $rootScope, $stateParams, previousState, entity, DietAppointment, Client, Dietetician) {
        var vm = this;

        vm.dietAppointment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('inzynierkaApp:dietAppointmentUpdate', function(event, result) {
            vm.dietAppointment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
