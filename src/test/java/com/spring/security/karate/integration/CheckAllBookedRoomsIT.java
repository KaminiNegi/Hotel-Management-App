package com.spring.security.karate.integration;

import com.intuit.karate.junit5.Karate;

public class CheckAllBookedRoomsIT {


        @Karate.Test

        Karate testLogin() {
            return Karate.run("classpath:karate/feature/checkallbookedrooms.feature").relativeTo(getClass());
        }


}
