package com.xpresspayment.javabackendexercise.utility;

import java.security.SecureRandom;

public class IdGenerator {

    public static Long generateId(){
        String templateNumber = "012345678901234";
        int count = 15;
        SecureRandom random = new SecureRandom();
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int randomIndex = random.nextInt(templateNumber.length());
            builder.append(templateNumber.charAt(randomIndex));
        }

        return Long.valueOf(builder.toString());
    }
}
