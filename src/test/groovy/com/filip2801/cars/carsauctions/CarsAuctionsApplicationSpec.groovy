package com.filip2801.cars.carsauctions

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.context.WebApplicationContext

class CarsAuctionsApplicationSpec extends ControllerIntegrationTestSpecification {

	@Autowired
	WebApplicationContext context

	def "should start application"() {
		expect: 'application started'
		context != null
	}

}
