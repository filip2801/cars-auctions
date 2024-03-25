package com.filip2801.cars.carsauctions.auction.infrastructure.messaging

import com.filip2801.cars.carsauctions.auction.domain.AuctionStatus
import com.filip2801.cars.carsauctions.auction.infrastructure.dto.AuctionDto
import com.filip2801.cars.carsauctions.testutils.SimpleIntegrationTestSpecification
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.QueueBuilder
import org.springframework.amqp.rabbit.core.RabbitAdmin
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired

import java.time.LocalDateTime

import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueId
import static com.filip2801.cars.carsauctions.testutils.TestUtils.uniqueString

class AuctionEventPublisherITSpec extends SimpleIntegrationTestSpecification {

    @Autowired
    RabbitAdmin rabbitAdmin
    @Autowired
    AuctionEventPublisher auctionEventPublisher

    @Autowired
    RabbitTemplate rabbitTemplate

    def "should send auction ended event"() {
        given:
        def queueName = uniqueString()
        createQueue(queueName, "auctions.events", "auction.ended");

        var auctionDto = someAuctionDto()

        when:
        auctionEventPublisher.publishAuctionEndedEvent(auctionDto)

        then:
        eventually {
            assert rabbitTemplate.receive(queueName) != null
        }
    }

    def "should send auction result satisfied event"() {
        given:
        def queueName = uniqueString()
        createQueue(queueName, "auctions.events", "auction.resultSatisfied");

        var auctionDto = someAuctionDto()

        when:
        auctionEventPublisher.publishAuctionResultSatisfied(auctionDto)

        then:
        eventually {
            assert rabbitTemplate.receive(queueName) != null
        }
    }

    def "should send auction result not satisfied event"() {
        given:
        def queueName = uniqueString()
        createQueue(queueName, "auctions.events", "auction.resultNotSatisfied");

        var auctionDto = someAuctionDto()

        when:
        auctionEventPublisher.publishAuctionResultNotSatisfied(auctionDto)

        then:
        eventually {
            assert rabbitTemplate.receive(queueName) != null
        }
    }

    void createQueue(String queueName, String exchange, String routingKey) {
        var queue = QueueBuilder.durable(queueName).build()
        Binding binding = BindingBuilder.bind(queue).to(new DirectExchange(exchange)).with(routingKey);
        rabbitAdmin.declareQueue(QueueBuilder.durable(queueName).build())
        rabbitAdmin.declareBinding(binding);
    }

    private AuctionDto someAuctionDto() {
        new AuctionDto(uniqueId(), uniqueId(), 'email@test.com', LocalDateTime.now(), LocalDateTime.now(), 100, null, null, AuctionStatus.RUNNING)
    }

}
