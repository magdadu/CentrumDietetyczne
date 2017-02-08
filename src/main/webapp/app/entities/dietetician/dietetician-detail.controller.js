(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DieteticianDetailController', DieteticianDetailController);

    DieteticianDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dietetician'];

    function DieteticianDetailController($scope, $rootScope, $stateParams, previousState, entity, Dietetician) {
        var vm = this;

        vm.dietetician = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('inzynierkaApp:dieteticianUpdate', function(event, result) {
            vm.dietetician = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
