package com.spring.springsecurity.utils;

import java.security.SecureRandom;
import java.util.Random;

public class OTPGenerator {
    private static final int OTP_LENGTH = 6;
    private static final int OTP_RANGE = (int) Math.pow(10, OTP_LENGTH);
    private static final int OTP_MIN = (int) Math.pow(10, OTP_LENGTH - 1);

    private static final Random random = new SecureRandom();

    public static int generateOTP() {
        return random.nextInt(OTP_RANGE - OTP_MIN) + OTP_MIN;
    }
}
