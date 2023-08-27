package com.rulin.base.execption;

/**
 * @apiNote 儒林小栈项目异常类
 * @author 程序儒
 * @date 2023/8/15
 * @version 1.0.0
 */
public class RuLinPlusException extends RuntimeException{
    private String errMessage;

    public RuLinPlusException() {
        super();
    }

    public RuLinPlusException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(CommonError commonError){
        throw new RuLinPlusException(commonError.getErrMessage());
    }
    public static void cast(String errMessage){
        throw new RuLinPlusException(errMessage);
    }
}
