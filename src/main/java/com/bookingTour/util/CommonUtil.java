package com.bookingTour.util;

import org.springframework.util.DigestUtils;

import java.util.List;

public class CommonUtil {

    public static boolean isEmpty(List<?> list) {
        return list == null || list.size() == 0;
    }

    public static String gravatarURL(String email) {
        String gravatarId = DigestUtils.md5DigestAsHex(email.getBytes());
        return "https://secure.gravatar.com/avatar/" + gravatarId + "?s=80";
    }

}
