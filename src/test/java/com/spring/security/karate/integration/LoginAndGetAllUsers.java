package com.spring.security.karate.integration;

import com.intuit.karate.junit5.Karate;

public class LoginAndGetAllUsers {


        @Karate.Test

        Karate testLogin() {
            return Karate.run("classpath:karate/feature/SearchAllUsers.feature").relativeTo(getClass());
        }


}
