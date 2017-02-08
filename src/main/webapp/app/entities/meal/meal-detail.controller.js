(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('MealDetailController', MealDetailController);

    MealDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Meal', 'Diet'];

    function MealDetailController($scope, $rootScope, $stateParams, previousState, entity, Meal, Diet) {
        var vm = this;

        vm.meal = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('inzynierkaApp:mealUpdate', function(event, result) {
            vm.meal = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
