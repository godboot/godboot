package com.godboot.app.model.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;

import com.godboot.framework.entity.BaseSearch;
import io.swagger.annotations.ApiModel;
import lombok.*;


/**
 * 应用对象 - AccountSearchDTO.
 * <p>
 * 对象中文名 - 账户.
 * <p>
 * 该类于 2019-12-25 15:24:29 生成，请勿手工修改！
 * </p>
 *
 * @see       AccountSearchDTO
 * @author    author
 * @version   1.0.0.0, 十二月 25, 2019
 * @copyright copyright
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AccountSearchDTO")
@Data
@NoArgsConstructor
public class AccountSearchDTO extends BaseSearch implements Serializable {
    public AccountSearchDTO(String id) {
        super(id);
    }

    @Override
    public String getOrderByStr() {
        return null;
    }
}

