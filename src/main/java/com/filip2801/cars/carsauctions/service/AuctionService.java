package com.filip2801.cars.carsauctions.service;

import com.filip2801.cars.carsauctions.dto.AuctionDto;
import com.filip2801.cars.carsauctions.dto.Builders;
import com.filip2801.cars.carsauctions.exception.BadRequestException;
import com.filip2801.cars.carsauctions.model.Auction;
import com.filip2801.cars.carsauctions.model.AuctionRepository;
import com.filip2801.cars.carsauctions.model.AuctionStatus;
import com.filip2801.cars.carsauctions.model.InspectionAppointmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class AuctionService {

    private static final int AUCTION_DURATION_MINUTES = 24;

    private final AuctionRepository auctionRepository;
    private final InspectionAppointmentService inspectionAppointmentService;

    public AuctionDto startAuction(AuctionDto auctionDto) {
        validateInspectionStatus(auctionDto.carId());

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime expectedEndTime = startTime.plusMinutes(AUCTION_DURATION_MINUTES);

        Auction auction = Auction.builder()
                .carId(auctionDto.carId())
                .customerEmailAddress(auctionDto.customerEmailAddress())
                .startTime(startTime)
                .expectedEndTime(expectedEndTime)
                .anchorBid(auctionDto.anchorBid())
                .status(AuctionStatus.RUNNING)
                .build();

        auctionRepository.save(auction);

        return Builders.toAuctionDto(auction);
    }

    private void validateInspectionStatus(Long carId) {
        var appointment = inspectionAppointmentService.findByCarId(carId);
        if (appointment.isEmpty() || appointment.get().getStatus() != InspectionAppointmentStatus.INSPECTION_SUCCESSFUL) {
            throw new BadRequestException();
        }
    }
}
