package com.spring.security.karate.integration;

import com.intuit.karate.junit5.Karate;

public class BookAndUnbookRoomIntegrationTest {


        @Karate.Test

        Karate testLogin() {
            return Karate.run("classpath:karate/feature/bookandunbook.feature").relativeTo(getClass());
        }


}
