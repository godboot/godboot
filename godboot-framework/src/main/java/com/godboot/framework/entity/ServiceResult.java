package com.godboot.framework.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用对象 - ServiceResult.
 * <p>
 * <p>
 * 该类于 2018-04-11 09:50:11 首次生成，通用Serivce层返回结果封装对象。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 11, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
@Data
@ApiModel(value = "服务返回数据包装类")
public class ServiceResult<T> implements Serializable {
    /**
     * 请求是否成功
     */
    @ApiModelProperty(value = "请求是否成功")
    private Boolean success;

    /**
     * 返回值
     */
    @ApiModelProperty(value = "返回值")
    private Integer code;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T data;

    /**
     * 返回消息
     */
    @ApiModelProperty(value = "返回消息")
    private String message;

    /**
     * 无参构造方法
     */
    private ServiceResult() {
        this.success = Boolean.TRUE;
        this.code = 200;
    }

    public static ServiceResult ERROR(String message) {
        return new ServiceResult(Boolean.FALSE, message);
    }

    public static <T> ServiceResult ERROR(T data, String message) {
        return new ServiceResult(Boolean.FALSE, data, message);
    }

    public static <T> ServiceResult ERROR(Integer code, T data, String message) {
        return new ServiceResult(Boolean.FALSE, code, data, message);
    }

    public static ServiceResult ERROR(Integer code, String message) {
        return new ServiceResult(Boolean.FALSE, code, message);
    }

    public static <T> ServiceResult SUCCESS(T data) {
        return new ServiceResult(Boolean.TRUE, data);
    }

    public static <T> ServiceResult SUCCESS(T data, String message) {
        return new ServiceResult(Boolean.TRUE, data, message);
    }

    public static <T> ServiceResult SUCCESS(Integer code, T data, String message) {
        return new ServiceResult(Boolean.TRUE, code, data, message);
    }

    /**
     * 请求结果、返回消息构造方法
     *
     * @param success
     * @param message
     */
    private ServiceResult(Boolean success, String message) {
        this(success, null, null, message);
    }

    /**
     * 请求结果、返回消息构造方法
     *
     * @param success
     * @param data
     */
    private ServiceResult(Boolean success, T data) {
        this(success, null, data, null);
    }

    /**
     * 请求结果、返回值、返回消息构造方法
     *
     * @param success
     * @param code
     * @param message
     */
    private ServiceResult(Boolean success, Integer code, String message) {
        this(success, code, null, message);
    }

    /**
     * 请求结果、返回数据、返回消息构造方法
     *
     * @param success
     * @param data
     * @param message
     */
    private ServiceResult(Boolean success, T data, String message) {
        this(success, null, data, message);
    }

    /**
     * 请求结果、返回值、返回消息、返回数据构造方法
     *
     * @param success
     * @param code
     * @param message
     * @param data
     */
    private ServiceResult(Boolean success, Integer code, T data, String message) {
        this.success = success == null ? Boolean.TRUE : success;
        this.code = code == null ? 200 : code;
        this.data = data;
        this.message = message;
    }
}
