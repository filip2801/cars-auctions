package com.filip2801.cars.carsauctions.service;

import com.filip2801.cars.carsauctions.dto.AuctionBidDto;
import com.filip2801.cars.carsauctions.dto.AuctionDto;
import com.filip2801.cars.carsauctions.dto.Builders;
import com.filip2801.cars.carsauctions.exception.BadRequestException;
import com.filip2801.cars.carsauctions.model.*;
import com.filip2801.cars.carsauctions.repository.AuctionBidRepository;
import com.filip2801.cars.carsauctions.repository.AuctionRepository;
import com.filip2801.cars.carsauctions.web.security.CustomUserDetails;
import com.filip2801.cars.carsauctions.web.security.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuctionService {

    private static final int AUCTION_DURATION_MINUTES = 24;

    private final AuctionRepository auctionRepository;
    private final AuctionBidRepository auctionBidRepository;
    private final InspectionAppointmentService inspectionAppointmentService;

    public AuctionDto startAuction(AuctionDto auctionDto) {
        validateInspectionStatus(auctionDto.carId());

        Auction auction = Auction.start(
                auctionDto.carId(),
                auctionDto.customerEmailAddress(),
                auctionDto.anchorBid());

        auctionRepository.save(auction);

        return Builders.toAuctionDto(auction);
    }

    private void validateInspectionStatus(Long carId) {
        var appointment = inspectionAppointmentService.findByCarId(carId);
        if (appointment.isEmpty() || appointment.get().getStatus() != InspectionAppointmentStatus.INSPECTION_SUCCESSFUL) {
            throw new BadRequestException();
        }
    }

    @Transactional
    @Retryable(retryFor = ObjectOptimisticLockingFailureException.class, maxAttempts = 10)
    public AuctionBidDto makeBid(Long auctionId, Integer bidValue) {
        CustomUserDetails loggedInDealer = UserContextHolder.getLoggedInUser();

        Auction auction = auctionRepository.findById(auctionId).orElseThrow(BadRequestException::new);

        BidResult bidResult = auction.makeBid(loggedInDealer.getUserId(), bidValue);

        if (bidResult.bidStatus() == AuctionBidStatus.MADE) {
            auctionRepository.save(auction);
        }

        AuctionBid bid = AuctionBid.builder()
                .auctionId(auction.getId())
                .dealerId(loggedInDealer.getUserId())
                .bidValue(bidValue)
                .status(bidResult.bidStatus())
                .time(bidResult.time())
                .build();
        auctionBidRepository.save(bid);

        return Builders.toAuctionBitDto(bid);
    }

}
