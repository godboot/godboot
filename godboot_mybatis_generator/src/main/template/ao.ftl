package ${appObjectPackage};

import java.io.Serializable;
import ${entityPackage}.${entityName};

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 应用对象 - ${entityName}AO.
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       ${entityName}AO
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
@JsonSerialize
public final class ${entityName}AO extends ${entityName} implements Serializable {
    /**
     * 默认的序列化 id.
     */
    private static final long serialVersionUID = 1L;
}
