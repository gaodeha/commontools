package com.wudi.datou.common.util;

import java.util.regex.Pattern;

public class CommonUtils {

    public static boolean isEmailFormat(String str) {
        String regx = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        Pattern p = Pattern.compile(regx);
        return p.matcher(str).matches();
    }
}
