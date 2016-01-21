package com.learningstarz.myflashcards.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ZahARin on 11.01.2016.
 */
public class Tools {
    //ExtraTags
    public static final String firstActivity_userClassExtraTag = "uclextra";
    public static final String firstActivity_userClassNameExtraTag = "uclnmextra";
    public static final String firstActivity_userExtraTag = "userextra";

    public static final String jsonObjectData = "data";
    public static final String jsonObjectResult = "result";
    public static final String jsonObjectStatus = "status";

    public static final int errLogIn = 111;
    public static final int errDelete = 105;
    public static final int errFieldsCannotBeEmpty = 102;
    public static final int errInvToken = 122;
    public static final int errOk = 200;
    public static final int errReserved = 1000;
    public static final int errUserNotEnrolled = 1001;
    public static final int errInvDeckId = 1002;
    public static final int errFuncUnderConstruct = 1003;

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    static Pattern pattern = Pattern.compile(EMAIL_PATTERN);


    public static boolean validateEmail(String str) {
        Matcher matcher = pattern.matcher(str);
        return matcher.matches();
    }
}
