(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('DietDetailController', DietDetailController);

    DietDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Diet','Diet_product','Meal'];

    function DietDetailController($scope, $rootScope, $stateParams, previousState, entity, Diet,Diet_product, Meal) {
        var vm = this;

        vm.diet = entity;
        vm.previousState = previousState.name;
        vm.diet_products={};
         vm.diet_products_1=[];
          vm.diet_products_2=[];
           vm.diet_products_3=[];
           vm.meals= Meal.query();
        getDietInfo();
        var unsubscribe = $rootScope.$on('inzynierkaApp:dietUpdate', function(event, result) {
            vm.diet = result;
        });

        function getMeals(){
            Meal.query(function(result) {
                vm.meals=result;
                for(var i=0;i<vm.meals.length;i++){
                    console.log(vm.meals[i].diet);
                if(vm.meals[i].diet.id==vm.diet.id)
                {
                    console.log(vm.diet_products[i].diet.id);
                }


                }
            });
            }


         function getDietInfo(){
         Diet_product.query(function(result) {
                                    vm.diet_products = result;
                                   // console.log(vm.diet.id);
                                   // vm.diet_products_1= result;
                                   for(var i=0;i<vm.diet_products.length;i++)
                                                                       {
                                                                       if(vm.diet_products[i].diet.id==vm.diet.id){
                                                                       //console.log(vm.diet_products[i].diet.id);
                                                                       var recomended=vm.diet_products[i].isRecommended;

                                                                        if(recomended==1)
                                                                        {vm.diet_products_1.push(vm.diet_products[i]);
                                                                       }
                                                                        else if(recomended==2)
                                                                        {vm.diet_products_2.push(vm.diet_products[i]);
                                                                        }
                                                                         else if(recomended==3)
                                                                         {vm.diet_products_3.push(vm.diet_products[i]);
                                                                         }

                                                                                    }
                                                                       }
                                });
 //console.log(vm.diet_products);

                                }
        $scope.$on('$destroy', unsubscribe);
    }
})();
