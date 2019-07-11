package com.company.chess_online_bakend_api.util;

public class StringUtils {

    // TODO: 2019-07-11 write test

    public static String capitalizeFirstLetter(String original) {
        if (original == null || original.length() == 0) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1);
    }
}
