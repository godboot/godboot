package com.godboot.framework.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 应用对象 - BaseEnumResult.
 * <p>
 * <p>
 * 该类于 2018-04-11 09:50:11 首次生成，通用分页对象。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 11, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
@ApiModel(value = "分页对象")
@Data
public final class Page implements Serializable {
    /**
     * 默认的序列化版本 id.
     */
    private static final long serialVersionUID = 1L;

    /**
     * 分页查询开始记录位置.
     */
    @ApiModelProperty(value = "分页查询开始记录位置")
    private Integer begin;

    /**
     * 分页查看下结束位置.
     */
    @ApiModelProperty(value = "分页查看下结束位置")
    private Integer end;

    /**
     * 每页显示记录数.
     */
    @ApiModelProperty(value = "每页显示记录数")
    private Integer length = 20;

    /**
     * 查询结果总记录数.
     */
    @ApiModelProperty(value = "查询结果总记录数")
    private Integer totalRecords;

    /**
     * 当前页码.
     */
    @ApiModelProperty(value = "当前页码")
    private Integer pageNo;

    /**
     * 总共页数.
     */
    @ApiModelProperty(value = "总共页数")
    private Integer pageCount;

    public Page() {

    }

    /**
     * 构造函数.
     *
     * @param begin
     * @param length
     */
    public Page(Integer begin, Integer length) {
        this.begin = begin;
        this.length = length;
        this.end = this.begin + this.length;
        this.pageNo = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
    }

    /**
     * @param begin
     * @param length
     * @param totalRecords
     */
    public Page(Integer begin, Integer length, Integer totalRecords) {
        this(begin, length);
        this.totalRecords = totalRecords;
    }

    /**
     * 设置页数，自动计算数据范围.
     *
     * @param pageNo
     */
    public Page(Integer pageNo) {
        this.pageNo = pageNo;
        pageNo = pageNo > 0 ? pageNo : 1;
        this.begin = this.length * (pageNo - 1);
        this.end = this.length * pageNo;
    }

    /**
     * @return the begin
     */
    public Integer getBegin() {
        return begin;
    }

    /**
     * @return the end
     */
    public Integer getEnd() {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(Integer end) {
        this.end = end;
    }

    /**
     * @param begin the begin to set
     */
    public void setBegin(Integer begin) {
        this.begin = begin;
        if (this.length != 0) {
            this.pageNo = (int) Math.floor((this.begin * 1.0d) / this.length) + 1;
        }
    }

    /**
     * @return the length
     */
    public Integer getLength() {
        return length;
    }

    /**
     * @param length the length to set
     */
    public void setLength(Integer length) {
        this.length = length;
    }

    /**
     * @return the totalRecords
     */
    public Integer getTotalRecords() {
        return totalRecords;
    }

    /**
     * @param totalRecords the totalRecords to set
     */
    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
        this.pageCount = (int) Math.floor((this.totalRecords * 1.0d) / this.length);
        if (this.totalRecords % this.length != 0) {
            this.pageCount++;
        }
    }

    /**
     * @return the pageNo
     */
    public Integer getPageNo() {
        return pageNo;
    }

    /**
     * @param pageNo the pageNo to set
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
        pageNo = pageNo > 0 ? pageNo : 1;
        this.begin = this.length * (pageNo - 1);
        this.end = this.length * pageNo;
    }

    /**
     * @return the pageCount
     */
    public Integer getPageCount() {
        if (pageCount == 0) {
            return 1;
        }
        return pageCount;
    }

    /**
     * @param pageCount the pageCount to set
     */
    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }
}

