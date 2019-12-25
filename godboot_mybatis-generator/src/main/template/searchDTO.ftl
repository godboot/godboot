package ${searchDTOPackage};

import com.ludwig.framework.entity.BaseSearch;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import lombok.*;


/**
 * 应用对象 - ${entityName}SearchDTO.
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 生成，请勿手工修改！
 * </p>
 *
 * @see       ${entityName}SearchDTO
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "${entityName}SearchDTO")
@Data
@NoArgsConstructor
public class ${entityName}SearchDTO extends BaseSearch implements Serializable {
    public ${entityName}SearchDTO(String id) {
        super(id);
    }

    @Override
    public String getOrderByStr() {
        return null;
    }
}

