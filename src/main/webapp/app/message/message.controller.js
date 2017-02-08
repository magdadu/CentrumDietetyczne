(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('MessageController', MessageController);

    MessageController.$inject = ['Message'];

    function MessageController (Message) {
        var vm = this;
        vm.messages = [];
        initMyMessages();

        function initMyMessages(){
        vm.messages = Message.query();
        console.log(vm.messages);

        }


    }
})();
