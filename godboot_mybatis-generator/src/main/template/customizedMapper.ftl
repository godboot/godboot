package ${customizedMapperPackage};

import ${dtoPackage}.${entityName}DTO;
import ${searchDTOPackage}.${entityName}SearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 应用对象 - ${entityName}CustomizedMapper.
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       ${entityName}CustomizedMapper
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
@Mapper
public interface ${entityName}CustomizedMapper {
    /**
    * 根据searchDTO查询${schema}列表
    *
    * @param searchDTO 查询条件
    * @return
    */
    List<${entityName}DTO> select${entityName}ListBySearchDTO(@Param("searchDTO") ${entityName}SearchDTO searchDTO);

    /**
    * 根据searchDTO查询${schema}数量
    *
    * @param searchDTO 查询条件
    * @return
    */
    Integer select${entityName}CountBySearchDTO(@Param("searchDTO") ${entityName}SearchDTO searchDTO);

    /**
    * 根据searchDTO查询${schema}详情
    *
    * @param searchDTO 查询条件
    * @return
    */
    ${entityName}DTO select${entityName}DetailBySearchDTO(@Param("searchDTO") ${entityName}SearchDTO searchDTO);
}
