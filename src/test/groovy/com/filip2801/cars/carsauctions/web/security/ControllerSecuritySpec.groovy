package com.filip2801.cars.carsauctions.web.security

import com.tngtech.archunit.core.importer.ClassFileImporter
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import spock.lang.Specification

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods

class ControllerSecuritySpec extends Specification {

    def "should all controller methods has security config"() {
        when:
        methods()
                .that().areAnnotatedWith(GetMapping.class)
                .or().areAnnotatedWith(PostMapping.class)
                .or().areAnnotatedWith(PutMapping.class)
                .or().areAnnotatedWith(DeleteMapping.class)
                .or().areAnnotatedWith(RequestMapping.class)
                .should().beAnnotatedWith(PreAuthorize.class)
                .check(new ClassFileImporter().importPackages("com.filip2801.cars.carsauctions"))

        then:
        noExceptionThrown()
    }

}
