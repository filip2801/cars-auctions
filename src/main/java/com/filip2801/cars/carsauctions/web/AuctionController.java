package com.filip2801.cars.carsauctions.web;


import com.filip2801.cars.carsauctions.dto.AuctionDto;
import com.filip2801.cars.carsauctions.dto.UserDto;
import com.filip2801.cars.carsauctions.service.AuctionService;
import com.filip2801.cars.carsauctions.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
