'use strict';

describe('Controller Tests', function() {

    describe('DietAppointment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDietAppointment, MockClient, MockDietetician;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDietAppointment = jasmine.createSpy('MockDietAppointment');
            MockClient = jasmine.createSpy('MockClient');
            MockDietetician = jasmine.createSpy('MockDietetician');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'DietAppointment': MockDietAppointment,
                'Client': MockClient,
                'Dietetician': MockDietetician
            };
            createController = function() {
                $injector.get('$controller')("DietAppointmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inzynierkaApp:dietAppointmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
