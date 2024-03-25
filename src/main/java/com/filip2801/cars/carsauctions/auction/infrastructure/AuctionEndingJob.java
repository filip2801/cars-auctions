package com.filip2801.cars.carsauctions.auction.infrastructure;

import com.filip2801.cars.carsauctions.auction.domain.AuctionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuctionEndingJob implements Job {

    private final AuctionService auctionService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        log.info("Starting job");

        auctionService.endExpiredAuctions();

        log.info("Job finished");
    }
}
