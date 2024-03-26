package com.filip2801.cars.carsauctions.inspection.infrastructure

import com.filip2801.cars.carsauctions.testutils.ControllerIntegrationTestSpecification
import org.springframework.http.HttpStatus

import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId

class InspectionAppointmentsControllerITSpec extends ControllerIntegrationTestSpecification {

    def "should book inspection appointment"() {
        given:
        def requestPayload = someAppointmentRequestPayload()

        when:
        var bookedInspectionAppointmentResponse = sendPost("inspection-appointments/booking", requestPayload)

        then:
        bookedInspectionAppointmentResponse.statusCode == HttpStatus.OK
        bookedInspectionAppointmentResponse.body.appointmentId
        bookedInspectionAppointmentResponse.body.locationId == requestPayload.locationId
        bookedInspectionAppointmentResponse.body.time == requestPayload.time
        bookedInspectionAppointmentResponse.body.status == 'BOOKED'
        bookedInspectionAppointmentResponse.body.car.id
        bookedInspectionAppointmentResponse.body.car.customerEmailAddress == requestPayload.car.customerEmailAddress
        bookedInspectionAppointmentResponse.body.car.makeId == requestPayload.car.makeId
        bookedInspectionAppointmentResponse.body.car.modelId == requestPayload.car.modelId
        bookedInspectionAppointmentResponse.body.car.variantId == requestPayload.car.variantId
        bookedInspectionAppointmentResponse.body.car.manufacturingYear == requestPayload.car.manufacturingYear
        bookedInspectionAppointmentResponse.body.car.registrationYear == requestPayload.car.registrationYear
        bookedInspectionAppointmentResponse.body.car.status == 'TO_SELL'
    }

    def "should fetch appointment"() {
        given:
        var appointmentId = bookAppointment()

        when:
        var appointmentResponse = sendGetForObject("inspection-appointments/$appointmentId")

        then:
        appointmentResponse.statusCode == HttpStatus.OK
        appointmentResponse.body.id == appointmentId
    }

    def "should return 404 when appointment does not exist"() {
        when:
        sendGetForObject("inspection-appointments/${uniqueId()}")

        then:
        thrown status404()
    }

    def "should finalise inspection"() {
        given:
        mockAgentUser()
        var appointmentId = bookAppointment()

        when:
        var changedStatusResponse = sendPut("inspection-appointments/$appointmentId/status", ['status': 'INSPECTION_SUCCESSFUL'])

        then:
        changedStatusResponse.statusCode == HttpStatus.OK
        changedStatusResponse.body.status == 'INSPECTION_SUCCESSFUL'

        and:
        var updatedAppointmentResponse = sendGetForObject("inspection-appointments/$appointmentId");
        updatedAppointmentResponse.body.status == 'INSPECTION_SUCCESSFUL'
    }

    def "should not allow to change status again to BOOKED"() {
        given:
        mockAgentUser()
        var appointmentId = bookAppointment()
        sendPut("inspection-appointments/$appointmentId/status", ['status': 'INSPECTION_SUCCESSFUL'])

        when:
        sendPut("inspection-appointments/$appointmentId/status", ['status': 'BOOKED'])

        then:
        thrown status400()
    }

    def "should not finalise inspection as not logged in user"() {
        given:
        var appointmentId = bookAppointment()

        when:
        sendPut("inspection-appointments/$appointmentId/status", ['status': 'INSPECTION_SUCCESSFUL'])

        then:
        thrown status401()
    }

    def "should not finalise inspection as dealer user"() {
        given:
        var appointmentId = bookAppointment()
        mockDealerUser()

        when:
        sendPut("inspection-appointments/$appointmentId/status", ['status': 'INSPECTION_SUCCESSFUL'])

        then:
        thrown status403()
    }

    def bookAppointment() {
        def bookInspectionAppointmentRequest = someAppointmentRequestPayload()
        var bookedInspectionAppointmentResponse = sendPost("inspection-appointments/booking", bookInspectionAppointmentRequest)
        return bookedInspectionAppointmentResponse.body.appointmentId
    }

    private LinkedHashMap<String, Serializable> someAppointmentRequestPayload() {
        return [
                locationId: uniqueId(),
                time      : '2024-05-15T14:30:00',
                car       : [
                        customerEmailAddress: 'test@customer.com',
                        makeId              : uniqueId(),
                        modelId             : uniqueId(),
                        variantId           : uniqueId(),
                        manufacturingYear   : 2015,
                        registrationYear    : 2016
                ]
        ]
    }

}