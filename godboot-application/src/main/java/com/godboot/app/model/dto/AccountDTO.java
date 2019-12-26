package com.godboot.app.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;


/**
 * 应用对象 - AccountDTO.
 * <p>
 * 对象中文名 - 账户.
 * <p>
 * 该类于 2019-12-25 15:17:01 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       AccountDTO
 * @author    author
 * @version   1.0.0.0, 十二月 25, 2019
 * @copyright copyright
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AccountDTO")
@Data
@NoArgsConstructor
@ToString
public class AccountDTO implements Serializable {
    /**
     * ID
     */
    @ApiModelProperty(value = "ID")
    private String id;

    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Integer userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String userName;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer status;

    /**
     * 删除状态
     */
    @ApiModelProperty(value = "删除状态")
    private Integer deleteStatus;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    public AccountDTO(Integer userId, String userName, String password, Integer status, Integer deleteStatus) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.status = status;
        this.deleteStatus = deleteStatus;
    }
}
