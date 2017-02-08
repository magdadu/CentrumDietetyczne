(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('ResultController', ResultController);

    ResultController.$inject = ['$scope', '$state', 'DataUtils', 'Result'];

    function ResultController ($scope, $state, DataUtils, Result) {
        var vm = this;

        vm.results = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;

        loadAll();

        function loadAll() {
            Result.query(function(result) {
                vm.results = result;
            });
        }
    }
})();
