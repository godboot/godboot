package com.godboot.framework.entity;


import com.godboot.framework.constant.DATA_ENUM;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 应用对象 - BaseEnumResult.
 * <p>
 * <p>
 * 该类于 2018-04-11 09:50:11 首次生成，基础查询对象。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 11, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
@ApiModel(value = "查询对象")
@Data
public abstract class BaseSearch implements Serializable {
    /**
     * 模糊查询
     */
    @ApiModelProperty(value = "模糊查询查询关键字")
    private String searchText;

    /**
     * 数据ID
     */
    @ApiModelProperty(value = "数据ID")
    private String id;

    /**
     * 数据ID列表
     */
    @ApiModelProperty(value = "数据ID列表")
    public List<String> idList;

    /**
     * 排除ID
     */
    @ApiModelProperty(value = "排除ID")
    private String excludeId;

    /**
     * 排除ID列表
     */
    @ApiModelProperty(value = "排除ID列表")
    public List<String> excludeIdList;

    /**
     * 数据状态
     */
    @ApiModelProperty(value = "数据状态")
    private Integer status;

    /**
     * 数据状态列表
     */
    @ApiModelProperty(value = "数据状态列表")
    private List<Integer> statusList;

    /**
     * 是否排除已经删除的
     */
    @ApiModelProperty(value = "是否排除已经删除的数据,默认true")
    private Boolean excludeDeleteStatus;

    /**
     * 是否需要有效状态
     */
    @ApiModelProperty(value = "是否需要有效状态,false")
    private Boolean enableStatus;

    /**
     * 全局隐藏状态(勿删除)
     */
    public static final Integer STATUS_HIDE = DATA_ENUM.STATUS.HIDE.code;

    /**
     * 全局启用状态(勿删除)
     */
    public static final Integer STATUS_ENABLE = DATA_ENUM.STATUS.ENABLE.code;

    /**
     * 全局停用状态(勿删除)
     */
    public static final Integer STATUS_DISABLE = DATA_ENUM.STATUS.DISABLE.code;

    /**
     * 全局YES状态(勿删除)
     */
    public static final Integer BOOL_YES = DATA_ENUM.BOOL_STATUS.YES.code;

    /**
     * 全局YES状态(勿删除)
     */
    public static final Integer BOOL_NO = DATA_ENUM.BOOL_STATUS.NO.code;

    /**
     * 分页对象
     */
    private Page page;

    /**
     * 排序字段
     */
    private String sort;

    /**
     * 排序规则
     */
    private String sortOrder;

    /**
     * 排序字符串
     */
    private String orderByStr;

    /**
     * 随机参数(不使用任何缓存效果)
     */
    @ApiModelProperty(value = "随机参数(不使用任何缓存效果)")
    private String r;

    /**
     * 无参构造
     */
    public BaseSearch() {
        this.idList = new ArrayList<>();
        this.excludeIdList = new ArrayList<>();
        this.excludeDeleteStatus = Boolean.TRUE;
        this.enableStatus = Boolean.FALSE;
        this.page = new Page(0, 20);
    }

    /**
     * ID构造器
     *
     * @param id 数据ID
     */
    public BaseSearch(String id) {
        this();
        this.id = id;
    }

    /**
     * ID、状态联合构造器
     *
     * @param id     数据ID
     * @param status 数据状态
     */
    public BaseSearch(String id, Integer status) {
        this();
        this.id = id;
        this.status = status;
    }

    /**
     * 子类实现抽象方法（实现自定义排序规则）
     *
     * @return 排序字符串对象
     */
    public abstract String getOrderByStr();
}
