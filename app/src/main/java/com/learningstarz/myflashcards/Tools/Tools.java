package com.learningstarz.myflashcards.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZahARin on 11.01.2016.
 */
public class Tools {
    public static final String userClassExtraTag = "uclextra";
    public static final String userClassNameExtraTag = "uclnmextra";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static Pattern pattern = Pattern.compile(EMAIL_PATTERN);


    public static boolean validateEmail(String str) {
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
