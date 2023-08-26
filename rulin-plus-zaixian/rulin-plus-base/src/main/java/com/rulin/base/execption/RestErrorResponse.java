package com.rulin.base.execption;

import java.io.Serializable;

/**
 * 错误响应参数包装
 *
 * @author 程序儒
 * @date 2023-08-15 14:14:53
 */
public class RestErrorResponse implements Serializable {
    private String errMessage;

    public RestErrorResponse(String errMessage) {
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
