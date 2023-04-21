package com.rulin.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static com.rulin.common.ResultCode.*;

/**
 * @author :  CodeScholar
 * @description : <p> 统一返回结果类 </p>
 */
@ApiModel(value = "统一返回结果类")
@Data
public class ResponseResult {
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "响应消息", required = false)
    private String message;

    /**
     * 响应码：参考`ResultCode`
     */
    @ApiModelProperty(value = "响应码", required = true)
    private Integer code;

    /**
     * 响应中的数据
     */
    @ApiModelProperty(value = "响应数据", required = false)
    private Object data;

    @ApiModelProperty(value = "响应数据", required = false)
    private Map<String, Object> extra = new HashMap<>();

    public ResponseResult putExtra(String key, Object value) {
        this.extra.put(key, value);
        return this;
    }

    public static ResponseResult error(String message) {
        return new ResponseResult(FAILURE.getCode(), message, null);
    }

    public static ResponseResult error() {
        return new ResponseResult(FAILURE.getCode(), ERROR.getDesc(), null);
    }

    public static ResponseResult error(Integer code, String message) {
        return new ResponseResult(code, message, null);
    }

    public static ResponseResult success() {
        return new ResponseResult(SUCCESS.getCode(), SUCCESS.getDesc(), null);
    }

    public static ResponseResult success(Object data) {
        return new ResponseResult(SUCCESS.getCode(), SUCCESS.getDesc(), data);
    }

    public static ResponseResult success(String message, Object data) {
        return new ResponseResult(SUCCESS.getCode(), message, data);
    }

    public static ResponseResult success(Integer code, String message, Object data) {
        return new ResponseResult(code, message, data);
    }

    public static ResponseResult success(Integer code, String message) {
        return new ResponseResult(code, message, null);
    }

    public ResponseResult(Integer code, String msg, Object data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }
}
