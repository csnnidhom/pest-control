package com.holding.pestcontrol.util;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class OtpUtils {

    public String generatorOtp(){
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        String output = Integer.toString(randomNumber);
        while (output.length() < 6){
            output = "0" + output;
        }
        return output;
    }
}
