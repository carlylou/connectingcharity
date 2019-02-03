package com.charityconnector.util;

import java.util.UUID;

public class CodeUtil {
    //general Unique active code
    public static String generateUniqueCode(){
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
