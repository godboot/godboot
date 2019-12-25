package ${controllerPackage};

import ${dtoPackage}.${entityName}DTO;
import ${searchDTOPackage}.${entityName}SearchDTO;
import ${servicePackage}.I${entityName}Service;
import com.echeng.biz.passport.model.authorize.UserInfoDTO;
import com.echeng.biz.passport.util.UserUtils;
import PageResult;
import ServiceResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import com.echeng.biz.franchise.log.annotation.Log;
import com.echeng.biz.franchise.log.annotation.LogAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * 应用对象 - ${entityName}Controller
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       ${entityName}Controller
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
@Api(value = "${humpEntityName}", description = "${schema}",tags = "${schema}")
@Controller
@RequestMapping("${humpEntityName}")
public class ${entityName}Controller {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private UserUtils userUtils;

    @Resource
    private I${entityName}Service ${humpEntityName}Service;

    @ApiOperation(value = "新建${schema}", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "add", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResult<String> add(HttpServletRequest request, @RequestBody @ApiParam ${entityName}DTO dto) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            return ${humpEntityName}Service.add${entityName}(dto, userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }

    @ApiOperation(value = "更新${schema}", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "update", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResult update(HttpServletRequest request, @RequestBody @ApiParam ${entityName}DTO dto) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            return ${humpEntityName}Service.update${entityName}(dto, userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }

    @ApiOperation(value = "删除${schema}", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "delete", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResult delete(HttpServletRequest request, @RequestBody @ApiParam ${entityName}SearchDTO searchDTO) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            return ${humpEntityName}Service.delete${entityName}(searchDTO, userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }

    @ApiOperation(value = "禁用${schema}", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "disable", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResult disable(HttpServletRequest request, @RequestBody @ApiParam ${entityName}SearchDTO searchDTO) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            return ${humpEntityName}Service.disable${entityName}(searchDTO, userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }

    @ApiOperation(value = "启用${schema}", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "enable", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ServiceResult enable(HttpServletRequest request , @RequestBody @ApiParam ${entityName}SearchDTO searchDTO) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            return ${humpEntityName}Service.enable${entityName}(searchDTO, userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }

    @ApiOperation(value = "获取${schema}信息", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @LogAction(Log.OFF)
    public ServiceResult<${entityName}DTO> getById(HttpServletRequest request, @RequestBody @ApiParam ${entityName}SearchDTO searchDTO) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            return ${humpEntityName}Service.get${entityName}(searchDTO,userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }

    @ApiOperation(value = "获取${schema}详情", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getDetailById", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @LogAction(Log.OFF)
    public ServiceResult<${entityName}DTO> getDetailById(HttpServletRequest request, @RequestBody @ApiParam ${entityName}SearchDTO searchDTO) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            return ${humpEntityName}Service.get${entityName}Detail(searchDTO, userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }

    @ApiOperation(value = "查询${schema}列表", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping(value = "getList", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @LogAction(Log.OFF)
    public ServiceResult<PageResult<List<${entityName}DTO>>> getList(HttpServletRequest request, @RequestBody @ApiParam ${entityName}SearchDTO searchDTO) {
        try {
            UserInfoDTO userInfoDTO = userUtils.getUserFromRequest(request);
            searchDTO.setOrgId(userInfoDTO.getOrgId());
            return ${humpEntityName}Service.get${entityName}List(searchDTO,userInfoDTO);
        } catch (Exception e) {
            logger.error(getClass().toString(), e);
            return ServiceResult.ERROR("操作失败 错误信息[" + e.getMessage() + "]");
        }
    }
}
