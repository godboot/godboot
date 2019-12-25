package com.godboot.framework.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用对象 - BaseEnumResult.
 * <p>
 * <p>
 * 该类于 2018-04-11 09:50:11 首次生成，通用分页返回对象。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 11, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
@ApiModel(value = "分页查询返回对象")
@Data
public class PageResult<T> implements Serializable {
    /**
     * 当前页码下标(从0开始)
     */
    @ApiModelProperty(value = "当前页码下标(从0开始)")
    private Integer pageIndex;

    /**
     * 当前返回数据条数
     */
    @ApiModelProperty(value = "当前返回数据条数")
    private Integer pageRows;

    /**
     * 总页数
     */
    @ApiModelProperty(value = "总页数")
    private Integer totalPages;

    /**
     * 总数据条数
     */
    @ApiModelProperty(value = "总数据条数")
    private Integer totalRows;

    /**
     * 返回数据
     */
    @ApiModelProperty(value = "返回数据")
    private T dataList;

    public PageResult(Integer pageIndex, Integer pageRows, Integer totalPages, Integer totalRows, T dataList) {
        this.pageIndex = pageIndex;
        this.pageRows = pageRows;
        this.totalPages = totalPages;
        this.totalRows = totalRows;
        this.dataList = dataList;
    }

    public PageResult(Integer totalRows, T dataList) {
        this.totalRows = totalRows;
        this.dataList = dataList;
    }
}
