package com.codesoom.project;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AppTest {

    @Test
    void appHasAGreeting() {
        App classUnderTest = new App();

        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
        assertThat(classUnderTest.getGreeting()).isEqualTo("Hello, world!");
    }
}
