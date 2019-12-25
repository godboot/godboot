package ${mapperPackage};

import ${appObjectPackage}.${entityNameWithBLOBsAO}AO;
import ${entityPackage}.${entityName}Criteria;
import com.ludwig.framework.plugin.mybatis.${baseGeneratedMapper};
import org.apache.ibatis.annotations.Mapper;

/**
 * 自动生成的 ${entityName} 数据存取接口.
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 生成，请勿手工修改！
 * </p>
 *
 * @see       ${entityName}GeneratedMapper
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
@Mapper
public interface ${entityName}GeneratedMapper extends ${baseGeneratedMapper}<${entityNameWithBLOBsAO}AO, ${entityName}Criteria> {

}
