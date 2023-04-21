package com.groupwork.charchar.common;

/**
 * @Author: wyl
 * @Description:
 * @Date: Created in 11:14 19/04/2023
 */
public class Constants {
    public enum ResponseCode {
        SUCCESS("0000", "success"),
        UN_ERROR("0001", "error");

        private String code;
        private String info;

        ResponseCode(String code, String info) {
            this.code = code;
            this.info = info;
        }

        public String getCode() {
            return code;
        }

        public String getInfo() {
            return info;
        }

    }
}
