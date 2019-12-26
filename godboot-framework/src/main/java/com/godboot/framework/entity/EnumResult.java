package com.godboot.framework.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

/**
 * 应用对象 - BaseEnumResult.
 * <p>
 * <p>
 * 该类于 2018-04-11 09:50:11 首次生成，通用枚举返回信息。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 11, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "枚举返回基本类型")
public class EnumResult<C, N> implements Serializable {
    /**
     * 编码
     */
    @ApiModelProperty(value = "编码")
    private C code;

    /**
     * 名称
     */
    @ApiModelProperty(value = "名称")
    private N name;
}
