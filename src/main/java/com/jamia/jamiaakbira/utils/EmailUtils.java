package com.jamia.jamiaakbira.utils;

public class EmailUtils {
    public static String getMailMessage(String name,String host,String key){
        return "Hello "+name+",\n\n" +
                "You have been invited to join Jamia Akbira. Please click on the link below to verify your email address.\n\n" +
                getVerificationUrl(host,key)+"\n\n" +
                "Thank you,\n" +
                "Jamia Akbira Team";
    }
    public static String getRestPasswordMessage(String name,String host,String key){
        return "Hello "+name+",\n\n" +
                "You have requested to reset your password. Please click on the link below to reset your password.\n\n" +
               getRestPasswordUrl(host,key)+"\n\n" +
                "Thank you,\n" +
                "Jamia Akbira Team";
    }

    private static String getVerificationUrl(String host, String key) {
        return host+"/verify/account?key="+key;
    } private static String getRestPasswordUrl(String host, String key) {
        return host+"/verify/password?key="+key;
    }
}
