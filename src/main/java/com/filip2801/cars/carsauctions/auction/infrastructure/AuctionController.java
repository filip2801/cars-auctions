package com.filip2801.cars.carsauctions.auction.infrastructure;

import com.filip2801.cars.carsauctions.auction.domain.AuctionService;
import com.filip2801.cars.carsauctions.auction.domain.AuctionStatus;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionBidDto;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionBidRequest;
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto;
import com.filip2801.cars.carsauctions.common.security.UserContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping(value = "/auctions", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AuctionController {

    private final AuctionService auctionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('AGENT')")
    AuctionDto startAuction(@RequestBody AuctionDto auctionDto) {
        return auctionService.startAuction(auctionDto);
    }

    @PostMapping(value = "/{auctionId}/bids", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DEALER')")
    AuctionBidDto makeBid(@PathVariable Long auctionId, @RequestBody AuctionBidRequest bidRequest) {
        var loggedInDealer = UserContextHolder.getLoggedInUser();
        Integer bidValue = bidRequest.bidValue();

        log.info("Making bid {} by dealer {} on auction {}", bidValue, loggedInDealer.getUserId(), auctionId);

        var auctionBidDto = auctionService.makeBid(auctionId, bidValue);

        log.info("Bid {} by dealer {} on auction {} ended as {} ", bidValue, loggedInDealer.getUserId(), auctionId, auctionBidDto.status());

        return auctionBidDto;
    }

    @PostMapping(value = "/{auctionId}/run-again", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('AGENT')")
    AuctionDto runAuctionAgain(@PathVariable Long auctionId) {
        return auctionService.runAgain(auctionId);
    }

    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('AGENT')")
    List<AuctionDto> findAllByStatus(@RequestParam AuctionStatus status) {
        return auctionService.findAllByStatus(status);
    }

}
