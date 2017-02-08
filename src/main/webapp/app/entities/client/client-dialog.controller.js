(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('ClientDialogController', ClientDialogController);

    ClientDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Client', 'User'];

    function ClientDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Client, User) {
        var vm = this;

        vm.client = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.client.id !== null) {
                Client.update(vm.client, onSaveSuccess, onSaveError);
            } else {
                Client.save(vm.client, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('inzynierkaApp:clientUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
