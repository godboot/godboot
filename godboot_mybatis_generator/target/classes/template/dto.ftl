package ${dtoPackage};

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiModel;
import lombok.Data;
${importContent}


/**
 * 应用对象 - ${entityName}DTO.
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       ${entityName}DTO
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "${entityName}DTO")
@Data
public class ${entityName}DTO implements Serializable {
    ${entityContent}
}
