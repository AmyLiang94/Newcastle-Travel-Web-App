package com.groupwork.charchar.exception;

import com.groupwork.charchar.common.Constants;

/**
 * @Author: wyl
 * @Description:
 * @Date: Created in 11:11 19/04/2023
 */
public class AttractionNotFoundException extends RuntimeException{

    private final String errorCode;

    public AttractionNotFoundException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

}
