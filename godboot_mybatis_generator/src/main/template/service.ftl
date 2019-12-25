package ${servicePackage}.impl;

import com.godboot.framework.entity.SessionUser;
import com.godboot.framework.constant.DATA_ENUM;
import com.godboot.framework.entity.PageResult;
import com.godboot.framework.entity.ServiceResult;
import java.util.List;
import ${dtoPackage}.*;
import ${searchDTOPackage}.*;


/**
 * 应用对象 - I${entityName}Service.
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       I${entityName}Service
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
public interface I${entityName}Service {
    /**
    * 获取${schema}列表
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @return
    */
    ServiceResult<PageResult<List<${entityName}DTO>>> get${entityName}List(${entityName}SearchDTO searchDTO, SessionUser user) throws InterruptedException;

    /**
    * 获取${schema}数量
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @return
    */
    ServiceResult<Integer> get${entityName}Count(${entityName}SearchDTO searchDTO, SessionUser user);

    /**
    * 删除${schema}信息
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @return
    */
    ServiceResult<Boolean> delete${entityName}(${entityName}SearchDTO searchDTO, SessionUser user);

    /**
    * 修改${schema}状态
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @param status    修改状态
    * @return
    */
    ServiceResult<Boolean> update${entityName}Status(${entityName}SearchDTO searchDTO, SessionUser user, DATA_ENUM.STATUS status);

    /**
    * 修改${schema}信息
    *
    * @param ${humpEntityName}DTO ${schema}信息
    * @param user    当前操作用户
    * @return
    */
    ServiceResult<Boolean> update${entityName}(${entityName}DTO ${humpEntityName}DTO, SessionUser user);

    /**
    * 添加${schema}信息
    *
    * @param ${humpEntityName}DTO ${schema}信息
    * @param user    当前操作用户
    * @return
    */
    ServiceResult<String> add${entityName}(${entityName}DTO ${humpEntityName}DTO, SessionUser user);

    /**
    * 查询${schema}信息
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @return
    */
    ServiceResult<${entityName}DTO> get${entityName}(${entityName}SearchDTO searchDTO, SessionUser user);

    /**
    * 禁用${schema}状态
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @return
    */
    ServiceResult<Boolean> disable${entityName}(${entityName}SearchDTO searchDTO, SessionUser user);

    /**
    * 启用${schema}状态
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @return
    */
    ServiceResult<Boolean> enable${entityName}(${entityName}SearchDTO searchDTO, SessionUser user);

    /**
    * 查询${schema}详情
    *
    * @param searchDTO 查询条件
    * @param user      当前操作用户
    * @return
    */
    ServiceResult<${entityName}DTO> get${entityName}Detail(${entityName}SearchDTO searchDTO, SessionUser user);
}

