package com.filip2801.cars.carsauctions.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.filip2801.cars.carsauctions.utils.Validate.validateIsTrue;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class InspectionAppointment {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Long locationId;
    @Column(nullable = false)
    private LocalDateTime time;
    @Column(nullable = false)
    private String customerEmailAddress;

    @Column(nullable = false)
    private Long carId;

    @Enumerated(EnumType.STRING)
    private InspectionAppointmentStatus status;

    public void finaliseInspection() {
        validateIsTrue(status == InspectionAppointmentStatus.BOOKED, "Cannot finalise inspection for status " + status);

        status = InspectionAppointmentStatus.INSPECTION_SUCCESSFUL;
    }
}
