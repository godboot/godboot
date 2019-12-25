package com.godboot.app.appobject;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.godboot.app.model.gen.mysql.Account;

import java.io.Serializable;

/**
 * 应用对象 - AccountAO.
 * <p>
 * 对象中文名 - 账户.
 * <p>
 * 该类于 2019-12-25 15:13:17 首次生成，后由开发手工维护。
 * </p>
 *
 * @author author
 * @version 1.0.0.0, 十二月 25, 2019
 * @copyright copyright
 * @see AccountAO
 */
@JsonSerialize
public final class AccountAO extends Account implements Serializable {
    /**
     * 默认的序列化 id.
     */
    private static final long serialVersionUID = 1L;
}
