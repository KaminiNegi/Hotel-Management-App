package com.spring.security.karate.integration;

import com.intuit.karate.junit5.Karate;
public class RegistrationAndLoginIntegration {


        @Karate.Test

        Karate testUserRegistration() {
            return Karate.run("classpath:karate/feature/registration.feature").relativeTo(getClass());
        }


}
