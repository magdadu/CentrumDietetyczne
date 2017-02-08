(function() {
    'use strict';

    angular
        .module('inzynierkaApp')
        .controller('dietAppointmentController', dietAppointmentController).
        directive('calendar',function(){
           return {
                   restrict: "E",
                   templateUrl: "app/dietAppointment/calendar.html",
                   scope: {
                       selected: "="
                   },
                   link: function(scope) {
                       scope.selected = _removeTime(scope.selected || moment());
                       scope.month = scope.selected.clone();

                       var start = scope.selected.clone();
                       start.date(1);
                       _removeTime(start.day(0));

                       _buildMonth(scope, start, scope.month);

                       scope.select = function(day) {
                           scope.selected = day.date;

                       };

                       scope.next = function() {
                           var next = scope.month.clone();
                           _removeTime(next.month(next.month()+1).date(1));
                           scope.month.month(scope.month.month()+1);
                           _buildMonth(scope, next, scope.month);
                       };

                       scope.previous = function() {
                           var previous = scope.month.clone();
                           _removeTime(previous.month(previous.month()-1).date(1));
                           scope.month.month(scope.month.month()-1);
                           _buildMonth(scope, previous, scope.month);
                       };
                   }
               };

               function _removeTime(date) {
                   return date.day(0).hour(0).minute(0).second(0).millisecond(0);
               }

               function _buildMonth(scope, start, month) {
                   scope.weeks = [];
                   var done = false, date = start.clone(), monthIndex = date.month(), count = 0;
                   while (!done) {
                       scope.weeks.push({ days: _buildWeek(date.clone(), month) });
                       date.add(1, "w");
                       done = count++ > 2 && monthIndex !== date.month();
                       monthIndex = date.month();
                   }
               }

               function _buildWeek(date, month) {
                   var days = [];
                   for (var i = 0; i < 7; i++) {
                       days.push({
                           name: date.format("dd").substring(0, 1),
                           number: date.date(),
                           isCurrentMonth: date.month() === month.month(),
                           isToday: date.isSame(new Date(), "day"),
                           date: date
                       });
                       date = date.clone();
                       date.add(1, "d");
                   }
                   return days;
               }


           });




    dietAppointmentController.$inject = ['$scope', 'Principal', 'Dietetician','Client','DietAppointment', '$state'];

    function dietAppointmentController ($scope, Principal, Dietetician,Client,DietAppointment, $state) {
                var vm = this;
                vm.day=0;
                vm.index=[];
                vm.dieteticians ={};
                vm.hours = [];
                vm.dietAppointment={};
                initDieteticians();
                initHours();

                function initDieteticians(){
                vm.dieteticians = Dietetician.query();


                }

                function initHours(){
                for (var i = 8; i < 21; i++) {

                vm.hours.push(i);

                }
                }
                $scope.initdietAppointment=function(){

                    DietAppointment.query().$promise.then(function success(data,headers){

                        vm.dietAppointments = data;
                        for(var i=0;i<vm.dietAppointments.length;i++){
                        var hour=vm.dietAppointments[i].hour;
                        var dietetician_id=vm.dietAppointments[i].dietetician.id;

                        if($scope.day.format('YYYY-MM-DD')==vm.dietAppointments[i].date)
                        {


                        document.getElementById("object-"+ hour +'-'+dietetician_id).textContent="Zarezerwowane";
                        document.getElementById("object-"+ hour +'-'+dietetician_id).style.backgroundColor = "red";
                        }
                        else{
                        document.getElementById("object-"+ hour +'-'+dietetician_id).textContent="Zarezerwuj";
                        document.getElementById("object-"+ hour +'-'+dietetician_id).style.backgroundColor = "#5bc0de";

                        }
                        }


                    }, function error(response){
                        console.log('Error during fetching diet appointments!')
                        console.log(response);

                    });
                    }

      $scope.reserve=function(x,y,day){

 if (document.getElementById("object-"+ x +'-'+y).textContent == "Zarezerwuj")
    {

       vm.dietAppointment.date=day.format("YYYY-MM-DD");
       vm.dietAppointment.hour=x;
       vm.dietAppointment.client=3;
        vm.dietAppointment.dietetician=y;

       console.log(vm.dietAppointment.client);
        console.log(vm.dietAppointment);
        DietAppointment.save(vm.dietAppointment);

        alert("Wizyta została zarezerwowana!");
        console.log("object-"+ x +'-'+y);
        document.getElementById("object-"+ x +'-'+y).textContent = "Zarezerwowane";
        document.getElementById("object-"+ x +'-'+y).style.backgroundColor = "red";

    }

                }
    }

})();
