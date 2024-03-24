package com.filip2801.cars.carsauctions.dto;

import com.filip2801.cars.carsauctions.model.*;

public class Builders {

    public static InspectionAppointmentBookingDto toAppointmentBookingDto(Car car, InspectionAppointment inspectionAppointment) {
        return new InspectionAppointmentBookingDto(
                inspectionAppointment.getId(),
                inspectionAppointment.getLocationId(),
                inspectionAppointment.getTime(),
                inspectionAppointment.getCustomerEmailAddress(),
                inspectionAppointment.getStatus(),
                toCarDto(car));
    }

    public static InspectionAppointmentDto toAppointmentDto(InspectionAppointment inspectionAppointment) {
        return new InspectionAppointmentDto(
                inspectionAppointment.getId(),
                inspectionAppointment.getLocationId(),
                inspectionAppointment.getTime(),
                inspectionAppointment.getCustomerEmailAddress(),
                inspectionAppointment.getStatus(),
                inspectionAppointment.getCarId());
    }

    public static CarDto toCarDto(Car car) {
        return new CarDto(
                car.getId(),
                car.getMakeId(),
                car.getModelId(),
                car.getVariantId(),
                car.getManufacturingYear(),
                car.getRegistrationYear());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getUsername(), null);
    }

    public static AuctionDto toAuctionDto(Auction auction) {
        return new AuctionDto(
                auction.getId(),
                auction.getCarId(),
                auction.getCustomerEmailAddress(),
                auction.getStartTime(),
                auction.getExpectedEndTime(),
                auction.getAnchorBid(),
                auction.getHighestBid(),
                auction.getLeadingDealerId(),
                auction.getStatus());
    }

    public static AuctionSubscriptionDto toAuctionSubscription(AuctionSubscription auctionSubscription) {
        return new AuctionSubscriptionDto(
                auctionSubscription.getId(),
                auctionSubscription.getCarMakeId(),
                auctionSubscription.getDealerId()
        );
    }

}
