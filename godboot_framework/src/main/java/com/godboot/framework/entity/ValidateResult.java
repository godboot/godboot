package com.godboot.framework.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用对象 - BaseEnumResult.
 * <p>
 * <p>
 * 该类于 2018-04-11 09:50:11 首次生成，通用Serivce层数据校验返回结果封装对象。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 11, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
@Data
@ApiModel(value = "数据验证返回类型")
public class ValidateResult<T> implements Serializable {
    /**
     * 请求是否成功
     */
    @ApiModelProperty(value = "请求是否成功")
    private Boolean success;

    /**
     * 返回消息
     */
    @ApiModelProperty(value = "返回消息")
    private String message;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T data;

    private ValidateResult() {

    }

    public static ValidateResult<Boolean> ERROR(String msg) {
        return new ValidateResult(Boolean.FALSE, Boolean.FALSE, msg);
    }

    public static ValidateResult SUCCESS(String msg) {
        return new ValidateResult(Boolean.TRUE, Boolean.TRUE, msg);
    }

    /**
     * 请求结果、返回消息构造方法
     *
     * @param success
     * @param message
     */
    public ValidateResult(Boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }
}