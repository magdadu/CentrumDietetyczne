(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('ResultDetailController', ResultDetailController);

    ResultDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'Result', 'Client'];

    function ResultDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, Result, Client) {
        var vm = this;

        vm.result = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('inzynierkaApp:resultUpdate', function(event, result) {
            vm.result = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
