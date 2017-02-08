(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('ClientController', ClientController);

    ClientController.$inject = ['$scope', '$state', 'Client'];

    function ClientController ($scope, $state, Client) {
        var vm = this;

        vm.clients = [];

        loadAll();

        function loadAll() {
            Client.query(function(result) {
                vm.clients = result;
            });
        }
    }
})();
