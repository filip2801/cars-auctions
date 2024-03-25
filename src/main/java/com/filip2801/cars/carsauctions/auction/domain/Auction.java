package com.filip2801.cars.carsauctions.auction.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import static com.filip2801.cars.carsauctions.common.Validate.validateIsTrue;

@Getter
@NoArgsConstructor
@Entity
public class Auction {

    private static final int EXTEND_TIME_IF_BID_IN_LAST_MINUTES = 2;
    private static final int LATE_BID_TIME_EXTENSION_MINUTES = 2;
    private static final int AUCTION_DURATION_MINUTES = 24;

    @Id
    @GeneratedValue
    private Long id;

    @Version
    private Long version;

    private Long carId;
    private String customerEmailAddress;
    private LocalDateTime startTime;
    private LocalDateTime expectedEndTime;
    private Integer anchorBid;
    private Integer highestBid;
    private Long leadingBidderId;

    @Enumerated(EnumType.STRING)
    private AuctionStatus status;

    public static Auction start(Long carId, String customerEmailAddress, Integer anchorBid) {
        return new Auction(carId, customerEmailAddress, anchorBid);
    }

    private Auction(Long carId, String customerEmailAddress, Integer anchorBid) {
        this.carId = carId;
        this.customerEmailAddress = customerEmailAddress;

        this.startTime = LocalDateTime.now();
        this.expectedEndTime = startTime.plusMinutes(AUCTION_DURATION_MINUTES);
        this.anchorBid = anchorBid;
        this.status = AuctionStatus.RUNNING;
    }

    public BidResult makeBid(Long bidderId, Integer bidValue) {
        LocalDateTime bidTime = LocalDateTime.now();
        if (isBidRejected(bidTime, bidValue)) {
            return new BidResult(AuctionBidStatus.REJECTED, bidTime);
        }

        this.leadingBidderId = bidderId;
        this.highestBid = bidValue;

        if (isLateBid(bidTime)) {
            extendAuctionTime();
        }

        return new BidResult(AuctionBidStatus.MADE, bidTime);
    }

    public void markAsEnded() {
        validateIsTrue(status == AuctionStatus.RUNNING, "Auction has status " + status);
        validateIsTrue(expectedEndTime.isBefore(LocalDateTime.now()), "Auction is not expired");

        this.status = AuctionStatus.ENDED;
    }

    private boolean isBidRejected(LocalDateTime bidTime, Integer bidValue) {
        return isBidLowerThanAnchor(bidValue)
                || isBidLowerThanHighestBid(bidValue)
                || isBidAfterEndTime(bidTime);
    }

    private boolean isBidLowerThanAnchor(Integer bidValue) {
        return bidValue < anchorBid;
    }

    private boolean isBidLowerThanHighestBid(Integer bidValue) {
        return highestBid != null && bidValue <= highestBid;
    }

    private boolean isBidAfterEndTime(LocalDateTime bidTime) {
        return bidTime.isAfter(expectedEndTime);
    }

    private void extendAuctionTime() {
        this.expectedEndTime = this.expectedEndTime.plusMinutes(EXTEND_TIME_IF_BID_IN_LAST_MINUTES);
    }

    private boolean isLateBid(LocalDateTime bidTime) {
        return bidTime.isAfter(expectedEndTime.minusMinutes(EXTEND_TIME_IF_BID_IN_LAST_MINUTES));
    }

    public void complete() {
        validateIsTrue(status == AuctionStatus.ENDED, "Auction has status " + status);
        this.status = AuctionStatus.COMPLETED;
    }

    public void finishWithoutSatisfiedResult() {
        validateIsTrue(status == AuctionStatus.ENDED, "Auction has status " + status);
        this.status = AuctionStatus.FINISHED_WITHOUT_WINNER;
    }
}
