package com.godboot.framework.util;

import com.godboot.framework.entity.ValidateResult;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Iterator;
import java.util.Set;

/**
 * 表单提交实体类字段校验工具类
 *
 * @author CHAGUO-PC
 */
public class BeanValidateUtil {
    /**
     * 验证某一个对象
     *
     * @param bean 参数
     * @return 错误提示
     */
    public static ValidateResult<Boolean> validate(Object bean) {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        /**
         * 验证某个对象,其实也可以只验证其中的某一个属性的
         */
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(bean);

        Iterator<ConstraintViolation<Object>> iter = constraintViolations.iterator();
        while (iter.hasNext()) {
            String message = iter.next().getMessage();
            return ValidateResult.ERROR(message);
        }
        return ValidateResult.SUCCESS("数据校验通过");
    }
}
