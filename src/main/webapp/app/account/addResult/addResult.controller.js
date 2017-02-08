/**
 * Created by Magda on 22.01.2017.
 */
(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('addResultController)',addResultController);

    addResultController.$inject = ['Message'];

    function addResultController(Message) {
        var vm = this;
        vm.messages = [];
        initMyMessages();

        function initMyMessages(){
            vm.messages = Message.query();
            console.log(vm.messages);

        }


    }
})();
