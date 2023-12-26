package com.personal.movie.util;


import com.personal.movie.domain.constants.ErrorCode;
import com.personal.movie.exception.CustomException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {

    public static String getCurrentMemberName() {
        final Authentication authentication = SecurityContextHolder.getContext()
            .getAuthentication();

        if (authentication == null || authentication.getName() == null) {
            throw new CustomException(ErrorCode.EMPTY_SECURITY_CONTEXT);
        }

        return authentication.getName();
    }
}
