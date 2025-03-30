package com.spring.security.karate.integration;

import com.intuit.karate.junit5.Karate;

public class LoginAndGetUserDetail {


        @Karate.Test

        Karate testLogin() {
            return Karate.run( 
            		
            	"classpath:karate/feature/login.feature"
             )
            		.relativeTo(getClass());
        }


}
