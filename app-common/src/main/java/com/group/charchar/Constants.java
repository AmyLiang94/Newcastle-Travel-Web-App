package com.group.charchar;


public class Constants {

    public enum ResponseCode {
        SUCCESS("0000", "success"),
        UN_ERROR("0001","fail");


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
