package com.takemypet.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

class ArchitectureTest {

    private static JavaClasses classes;

    @BeforeAll
    static void importClasses() {
        classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.takemypet");
    }

    @Test
    void layeredArchitectureIsRespected() {
        layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controller").definedBy("com.takemypet.controller..")
                .layer("Service").definedBy("com.takemypet.service..")
                .layer("Repository").definedBy("com.takemypet.repository..")
                .layer("Domain").definedBy("com.takemypet.domain..")
                .layer("Mapper").definedBy("com.takemypet.mapper..")
                .layer("DTO").definedBy("com.takemypet.dto..")
                .layer("Util").definedBy("com.takemypet.util..")
                .layer("Config").definedBy("com.takemypet.config..")

                .whereLayer("Controller").mayOnlyBeAccessedByLayers("Config")
                .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                .whereLayer("Repository").mayOnlyBeAccessedByLayers("Service")
                .whereLayer("Domain").mayOnlyBeAccessedByLayers(
                        "Controller", "Service", "Repository", "Mapper", "DTO", "Util", "Config")

                .check(classes);
    }

    @Test
    void controllersShouldOnlyDependOnServices() {
        noClasses()
                .that().resideInAPackage("com.takemypet.controller..")
                .should().dependOnClassesThat()
                .resideInAPackage("com.takemypet.repository..")
                .check(classes);
    }

    @Test
    void servicesShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("com.takemypet.service..")
                .should().dependOnClassesThat()
                .resideInAPackage("com.takemypet.controller..")
                .check(classes);
    }

    @Test
    void repositoriesShouldNotDependOnServices() {
        noClasses()
                .that().resideInAPackage("com.takemypet.repository..")
                .should().dependOnClassesThat()
                .resideInAPackage("com.takemypet.service..")
                .check(classes);
    }

    @Test
    void repositoriesShouldNotDependOnControllers() {
        noClasses()
                .that().resideInAPackage("com.takemypet.repository..")
                .should().dependOnClassesThat()
                .resideInAPackage("com.takemypet.controller..")
                .check(classes);
    }

    @Test
    void restControllersShouldBeAnnotated() {
        classes()
                .that().resideInAPackage("com.takemypet.controller..")
                .and().areNotInterfaces()
                .and().areNotAnnotatedWith(org.springframework.context.annotation.Configuration.class)
                .should().beAnnotatedWith(RestController.class)
                .check(classes);
    }

    @Test
    void serviceClassesShouldBeAnnotated() {
        classes()
                .that().resideInAPackage("com.takemypet.service..")
                .and().areNotInterfaces()
                .should().beAnnotatedWith(Service.class)
                .check(classes);
    }

    @Test
    void repositoryInterfacesShouldBeAnnotated() {
        classes()
                .that().resideInAPackage("com.takemypet.repository..")
                .and().areInterfaces()
                .should().beAnnotatedWith(Repository.class)
                .check(classes);
    }

    @Test
    void domainClassesShouldNotDependOnSpringWebAnnotations() {
        noClasses()
                .that().resideInAPackage("com.takemypet.domain..")
                .should().dependOnClassesThat()
                .resideInAPackage("org.springframework.web..")
                .check(classes);
    }

    @Test
    void noClassShouldUseFieldInjection() {
        noFields()
                .that().areDeclaredInClassesThat().resideInAPackage("com.takemypet.service..")
                .should().beAnnotatedWith(org.springframework.beans.factory.annotation.Autowired.class)
                .because("Constructor injection is required for testability and immutability")
                .check(classes);
    }
}
