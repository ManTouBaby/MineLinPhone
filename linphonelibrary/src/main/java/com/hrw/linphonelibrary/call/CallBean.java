package com.hrw.linphonelibrary.call;

/**
 * @version 1.0.0
 * @author:hrw
 * @date:2019/10/09 15:18
 * @desc:
 */
public class CallBean {
    private String callNumber;
    private String callName;


    public CallBean(String callNumber, String callName) {
        this.callNumber = callNumber;
        this.callName = callName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }
}
