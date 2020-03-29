package com.modosa.switchnightui.util;

/**
 * @author dadaewq
 */
class ResultMsg {
    private boolean success = false;
    private String msg;

    boolean isSuccess() {
        return success;
    }

    void setSuccess() {
        this.success = true;
    }

    String getMsg() {
        return msg;
    }

    void setMsg(String msg) {
        this.msg = msg;
    }

}
