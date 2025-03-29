package com.spring.security.karate.integration;

import com.intuit.karate.junit5.Karate;

public class LoginAndGetUserDetailTest {


        @Karate.Test

        Karate testLogin() {
            return Karate.run("classpath:karate/feature/login.feature").relativeTo(getClass());
        }


}
