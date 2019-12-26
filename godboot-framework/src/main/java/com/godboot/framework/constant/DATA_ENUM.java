package com.godboot.framework.constant;

/**
 * 全局数据状态枚举
 *
 * @author CHAGUO-PC
 */
public class DATA_ENUM {

    public enum STATUS {
        ENABLE(1, "启用"),
        DISABLE(0, "停用"),
        HIDE(98, "隐藏");

        public Integer code;

        public String name;

        STATUS(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static STATUS get(Integer code) {
            for (STATUS status : STATUS.values()) {
                if (code.equals(status.code)) {
                    return status;
                }
            }
            return ENABLE;
        }
    }

    public enum GENDER {
        MALE(1, "男"),
        FEMALE(2, "女"),
        OTHER(3, "未知");

        public Integer code;

        public String name;

        GENDER(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static GENDER get(Integer code) {
            for (GENDER status : GENDER.values()) {
                if (code.equals(status.code)) {
                    return status;
                }
            }
            return OTHER;
        }
    }

    public enum PAYMENT_STATUS {

        NO_PAY(1, "未缴费"),
        PAYED(2, "已缴费"),
        NEED_NOT_PAY(3, "不需要缴费"),
        REFUNDING(5, "退款中"),
        REFUND(4, "已退款");

        public Integer code;

        public String name;

        PAYMENT_STATUS(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static PAYMENT_STATUS get(Integer code) {
            for (PAYMENT_STATUS status : PAYMENT_STATUS.values()) {
                if (code.equals(status.code)) {
                    return status;
                }
            }
            return NO_PAY;
        }
    }

    public enum BOOL_STATUS {
        YES(1, "是"),
        NO(0, "否");

        public Integer code;

        public String name;

        BOOL_STATUS(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static String getName(Integer code) {
            for (BOOL_STATUS status : BOOL_STATUS.values()) {
                if (code.equals(status.code)) {
                    return status.name;
                }
            }
            return null;
        }

        public static BOOL_STATUS get(Integer code) {
            for (BOOL_STATUS status : BOOL_STATUS.values()) {
                if (code.equals(status.code)) {
                    return status;
                }
            }
            return NO;
        }
    }

    public enum OPERATE {
        INSERT(1, "新增"),
        UPDATE(2, "修改"),
        SELECT(3, "查询"),
        DELETE(4, "删除");

        public Integer code;

        public String name;

        OPERATE(Integer code, String name) {
            this.code = code;
            this.name = name;
        }

        public static OPERATE get(Integer code) {
            for (OPERATE status : OPERATE.values()) {
                if (code.equals(status.code)) {
                    return status;
                }
            }
            return INSERT;
        }
    }
}
