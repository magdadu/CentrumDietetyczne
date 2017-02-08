(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('ContactController', ContactController);

    ContactController.$inject = ['Contact','Message','$scope' ];


       function ContactController (Contact,Message,$scope) {
           var vm = this;
           vm.contact = {};
            vm.message= {};
           $scope.addMyContact=function(){

                console.log($scope.vm.contact);
                Contact.save($scope.vm.contact);
                Message.save($scope.vm.contact);
            }


       }
   })();
