(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DieteticianController', DieteticianController);

    DieteticianController.$inject = ['$scope', '$state', 'Dietetician'];

    function DieteticianController ($scope, $state, Dietetician) {
        var vm = this;

        vm.dieteticians = [];

        loadAll();

        function loadAll() {
            Dietetician.query(function(result) {
                vm.dieteticians = result;
            });
        }
    }
})();
