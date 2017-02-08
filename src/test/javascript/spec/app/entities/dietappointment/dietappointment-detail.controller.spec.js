'use strict';

describe('Controller Tests', function() {

    describe('Dietappointment Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockDietappointment, MockClient, MockDietetician;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockDietappointment = jasmine.createSpy('MockDietappointment');
            MockClient = jasmine.createSpy('MockClient');
            MockDietetician = jasmine.createSpy('MockDietetician');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Dietappointment': MockDietappointment,
                'Client': MockClient,
                'Dietetician': MockDietetician
            };
            createController = function() {
                $injector.get('$controller')("DietappointmentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'inzynierkaApp:dietappointmentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
