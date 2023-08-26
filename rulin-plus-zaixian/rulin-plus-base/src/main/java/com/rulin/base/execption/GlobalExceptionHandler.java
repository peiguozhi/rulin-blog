package com.rulin.base.execption;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;


/**
 * 全局异常处理器
 *
 * @author 程序儒
 * @date 2023-08-15 14:16:45
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuLinPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(RuLinPlusException e) {
        log.error("【系统异常】{}", e.getErrMessage(), e);
        return new RestErrorResponse(e.getErrMessage());

    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {
        log.error("【系统异常】{}", e.getMessage(), e);
        return new RestErrorResponse(CommonError.UNKNOWN_ERROR.getErrMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        List<String> msgList = new ArrayList<>();
        //将错误信息放在msgList
        bindingResult.getFieldErrors().stream().forEach(item->msgList.add(item.getDefaultMessage()));
        //拼接错误信息
        String msg = StringUtils.join(msgList, ",");
        log.error("【系统异常】{}",msg);
        return new RestErrorResponse(msg);
    }

}
