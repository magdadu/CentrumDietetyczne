(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DieteticianDialogController', DieteticianDialogController);

    DieteticianDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dietetician'];

    function DieteticianDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dietetician) {
        var vm = this;

        vm.dietetician = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dietetician.id !== null) {
                Dietetician.update(vm.dietetician, onSaveSuccess, onSaveError);
            } else {
                Dietetician.save(vm.dietetician, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('inzynierkaApp:dieteticianUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
