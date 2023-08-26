
package com.rulin.base.execption;


/**
 * 通用错误信息
 *
 * @author 程序儒
 * @date 2023-08-15 13:56:12
 */
public enum CommonError {

	/**
	 * 对象为空
	 */
	OBJECT_NULL("对象为空"),
	/**
	 * 非法参数
	 */
	PARAMS_ERROR("非法参数"),
	/**
	 * 查询结果为空
	 */
	QUERY_NULL("查询结果为空"),
	/**
	 * 请求参数为空
	 */
	REQUEST_NULL("请求参数为空"),
	/**
	 * 执行过程异常
	 */
	UNKNOWN_ERROR("执行过程异常，请重试。");

	private final String errMessage;

	public String getErrMessage() {
		return errMessage;
	}

	CommonError(String errMessage) {
		this.errMessage = errMessage;
	}

}
