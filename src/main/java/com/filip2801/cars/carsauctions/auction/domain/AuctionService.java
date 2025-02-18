package com.filip2801.cars.carsauctions.auction.domain;

import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionBidDto;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.Builders;
import com.filip2801.cars.carsauctions.auction.infrastructure.messaging.AuctionEventPublisher;
import com.filip2801.cars.carsauctions.common.exception.BadRequestException;
import com.filip2801.cars.carsauctions.common.exception.ResourceNotFoundException;
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
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final AuctionBidRepository auctionBidRepository;
    private final InspectionAppointmentService inspectionAppointmentService;
    private final AuctionEventPublisher auctionEventPublisher;

    @Transactional
    public AuctionDto startAuction(AuctionDto auctionDto) {
        validateInspectionStatus(auctionDto.carId());

        Auction auction = Auction.start(
                auctionDto.carId(),
                auctionDto.customerEmailAddress(),
                auctionDto.anchorBid());

        auctionRepository.save(auction);

        AuctionDto startedAuctionDto = Builders.toAuctionDto(auction);
        auctionEventPublisher.publishAuctionStartedEvent(startedAuctionDto);

        return startedAuctionDto;
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

    @Transactional
    public void endExpiredAuctions() {
        var auctionsToClose = auctionRepository.findAllByStatusAndExpectedEndTimeBefore(AuctionStatus.RUNNING, LocalDateTime.now());

        auctionsToClose.forEach(Auction::markAsEnded);
        auctionRepository.saveAll(auctionsToClose);

        auctionsToClose.forEach(auction -> {
            var auctionDto = Builders.toAuctionDto(auction);
            auctionEventPublisher.publishAuctionEndedEvent(auctionDto);
        });
    }

    public void finishWithSatisfiedResult(Long auctionId) {
        var auction = auctionRepository.findById(auctionId).orElseThrow(ResourceNotFoundException::new);
        auction.complete();
        auctionRepository.save(auction);

        auctionEventPublisher.publishAuctionCompletedEvent(Builders.toAuctionDto(auction));
    }

    public void finishWithNotSatisfiedResult(Long auctionId) {
        var auction = auctionRepository.findById(auctionId).orElseThrow(ResourceNotFoundException::new);
        auction.finishWithoutSatisfiedResult();
        auctionRepository.save(auction);
    }

    public AuctionDto runAgain(Long auctionId) {
        var firstAuction = auctionRepository.findById(auctionId).orElseThrow(ResourceNotFoundException::new);

        if (firstAuction.getStatus() != AuctionStatus.FINISHED_WITHOUT_WINNER) {
            throw new BadRequestException();
        }

        Auction newAuction = Auction.start(
                firstAuction.getCarId(),
                firstAuction.getCustomerEmailAddress(),
                firstAuction.getAnchorBid());

        auctionRepository.save(newAuction);

        return Builders.toAuctionDto(newAuction);
    }

    public List<AuctionDto> findAllByStatus(AuctionStatus status) {
        return auctionRepository.findAllByStatus(status).stream()
                .map(Builders::toAuctionDto)
                .collect(Collectors.toList());
    }

}
