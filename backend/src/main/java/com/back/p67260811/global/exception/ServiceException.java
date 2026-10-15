package com.back.p67260811.global.exception;

import com.back.p67260811.global.dto.RsData;

public class ServiceException extends RuntimeException {

    private RsData rsData;

    public ServiceException(String resultCode, String msg) {
        super(msg);
        this.rsData = new RsData(
                resultCode,
                msg
        );
    }

    public String getResultCode() {
        return rsData.getResultCode();
    }

    public String getMsg() {
        return rsData.getMsg();
    }

    public RsData getRsData() {
        return rsData;
    }
}
