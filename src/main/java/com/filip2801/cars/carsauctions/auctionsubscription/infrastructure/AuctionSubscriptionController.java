package com.filip2801.cars.carsauctions.auctionsubscription.infrastructure;

import com.filip2801.cars.carsauctions.auctionsubscription.domain.AuctionSubscriptionService;
import com.filip2801.cars.carsauctions.auctionsubscription.infrastructure.dto.AuctionSubscriptionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(value = "/auction-subscriptions", produces = MediaType.APPLICATION_JSON_VALUE)
@RestController
public class AuctionSubscriptionController {

    private final AuctionSubscriptionService auctionSubscriptionService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('DEALER')")
    AuctionSubscriptionDto subscribe(@RequestBody AuctionSubscriptionDto auctionSubscriptionDto) {
        return auctionSubscriptionService.subscribe(auctionSubscriptionDto.carMakeId());
    }

}
