package com.filip2801.cars.carsauctions.auction.infrastructure;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@RequiredArgsConstructor
@Component
public class AuctionEndingJobInitializer {

    private static final String JOB_NAME = "auction-ending-job";
    private static final String JOB_DESCRIPTION = "Auction ending job";

    private final Scheduler quartzScheduler;

    @PostConstruct
    public void scheduleJob() {
        try {
            JobDetail jobDetail = buildJobDetails();
            Trigger trigger = buildTrigger(jobDetail);

            if (triggerDoesNotExist(trigger.getKey())) {
                quartzScheduler.scheduleJob(jobDetail, trigger);
            }
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean triggerDoesNotExist(TriggerKey key) throws SchedulerException {
        return quartzScheduler.getTrigger(key) == null;
    }

    public JobDetail buildJobDetails() {
        return JobBuilder.newJob(AuctionEndingJob.class)
                .withIdentity("AuctionClosingJob", JOB_NAME)
                .withDescription(JOB_DESCRIPTION)
                .storeDurably(true)
                .build();
    }

    public Trigger buildTrigger(JobDetail jobDetail) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .withIdentity(jobDetail.getKey().getName(), JOB_NAME)
                .withDescription(JOB_DESCRIPTION)
                .startAt(new Date())
                .withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")
                        .withMisfireHandlingInstructionFireAndProceed())
                .build();
    }
}
