package com.godboot.framework.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

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
@ApiModel(value = "基础文件信息")
@Data
public class BaseFile implements Serializable {
    /**
     * id
     */
    @ApiModelProperty(value = "id")
    private String id;

    /**
     * 文件名称
     */
    @ApiModelProperty(value = "文件名称")
    private String fileName;

    /**
     * 文件扩展格式
     */
    @ApiModelProperty(value = "文件扩展格式")
    private String extName;

    /**
     * 文件大小
     */
    @ApiModelProperty(value = "文件大小")
    private Long size;

    /**
     * 链接地址
     */
    @ApiModelProperty(value = "链接地址")
    private String linkUrl;
}
