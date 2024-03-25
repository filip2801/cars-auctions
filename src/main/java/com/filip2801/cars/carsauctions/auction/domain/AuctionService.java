package com.filip2801.cars.carsauctions.auction.domain;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionBidDto;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.common.exception.BadRequestException;
import com.filip2801.cars.carsauctions.common.security.CustomUserDetails;
import com.filip2801.cars.carsauctions.common.security.UserContextHolder;
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentService;
import com.filip2801.cars.carsauctions.inspection.domain.InspectionAppointmentStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuctionService {

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
            // todo publish event, change other bids statuses
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

    public void endExpiredAuctions() {
        var auctionsToClose = auctionRepository.findAllByStatusAndExpectedEndTimeBefore(AuctionStatus.RUNNING, LocalDateTime.now());

        auctionsToClose.forEach(auction -> {
            auction.markAsEnded();
            // todo publish event
        });
    }
}
