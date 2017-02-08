(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('Diet_productDetailController', Diet_productDetailController);

    Diet_productDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Diet_product', 'Diet'];

    function Diet_productDetailController($scope, $rootScope, $stateParams, previousState, entity, Diet_product, Diet) {
        var vm = this;

        vm.diet_product = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('inzynierkaApp:diet_productUpdate', function(event, result) {
            vm.diet_product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
