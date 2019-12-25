package ${servicePackage};

import ${appObjectPackage}.${entityName}AO;
import ${customizedMapperPackage}.${entityName}CustomizedMapper;
import ${mapperPackage}.${entityName}GeneratedMapper;
import ${dtoPackage}.${entityName}DTO;
import ${entityPackage}.${entityName}Criteria;
import ${searchDTOPackage}.${entityName}SearchDTO;
import com.godboot.framework.entity.UserInfoDTO;
import com.godboot.framework.constant.DATA_ENUM;
import com.godboot.framework.entity.PageResult;
import com.godboot.framework.entity.ServiceResult;
import com.godboot.framework.entity.ValidateResult;
import com.godboot.framework.util.BeanValidateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 应用对象 - ${entityName}ServiceImpl.
 * <p>
 * 对象中文名 - ${schema}.
 * <p>
 * 该类于 ${dateTime} 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       ${entityName}ServiceImpl
 * @author    ${author}
 * @version   1.0.0.0, ${date}
 * @copyright ${copyright}
 */
@Service
@Slf4j
public class ${entityName}ServiceImpl implements I${entityName}Service {
    @Resource
    private ${entityName}GeneratedMapper ${humpEntityName}GeneratedMapper;

    @Resource
    private ${entityName}CustomizedMapper ${humpEntityName}CustomizedMapper;

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public ServiceResult<PageResult<List<${entityName}DTO>>> get${entityName}List(final ${entityName}SearchDTO searchDTO, final UserInfoDTO user) throws InterruptedException {
        AtomicInteger total = new AtomicInteger();
        AtomicReference<List<${entityName}DTO>> dtoList = new AtomicReference<>();

        CountDownLatch countDownLatch = new CountDownLatch(2);

        threadPoolTaskExecutor.execute(() -> {
            total.set(getListCount(searchDTO));
            countDownLatch.countDown();
        });

        threadPoolTaskExecutor.execute(() -> {
            dtoList.set(${humpEntityName}CustomizedMapper.select${entityName}ListBySearchDTO(searchDTO));
            countDownLatch.countDown();
        });

        countDownLatch.await();

        return ServiceResult.SUCCESS(new PageResult<>(total.get(), dtoList.get()), "获取${schema}列表成功");
    }

    @Override
    public ServiceResult<Integer> get${entityName}Count(${entityName}SearchDTO searchDTO, UserInfoDTO user) {
        return ServiceResult.SUCCESS(getListCount(searchDTO), "获取${schema}数量成功");
    }

    private Integer getListCount(${entityName}SearchDTO searchDTO) {
        Integer total = ${humpEntityName}CustomizedMapper.select${entityName}CountBySearchDTO(searchDTO);
        return total;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> delete${entityName}(${entityName}SearchDTO searchDTO, UserInfoDTO user) {
        ServiceResult<${entityName}DTO> serviceResult = get${entityName}(searchDTO, user);
        if (!serviceResult.getSuccess() || serviceResult.getData() == null) {
            return ServiceResult.ERROR(serviceResult.getMessage());
        }

        ${entityName}AO ao = new ${entityName}AO();
        ao.setId(searchDTO.getId());

        //ao.setDeleteStatus(DATA_ENUM.BOOL_STATUS.YES.code+"");
        //ao.setOperateUserId(user.getId());
        //ao.setOperateUserName(user.getName());
        //ao.setOperateTime(new Date());

        ${humpEntityName}GeneratedMapper.updateByPrimaryKeySelective(ao);

        return ServiceResult.SUCCESS(Boolean.TRUE, "删除${schema}成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> update${entityName}Status(${entityName}SearchDTO searchDTO, UserInfoDTO user, DATA_ENUM.STATUS status) {
        return update${entityName}Status(searchDTO, user, status, "修改${schema}状态成功");
    }

    private ServiceResult<Boolean> update${entityName}Status(final ${entityName}SearchDTO searchDTO, final UserInfoDTO user, final DATA_ENUM.STATUS status, final String message) {
        ServiceResult<${entityName}DTO> serviceResult = get${entityName}(searchDTO, user);
        if (!serviceResult.getSuccess() || serviceResult.getData() == null) {
            return ServiceResult.ERROR(serviceResult.getMessage());
        }

        ${entityName}DTO ${humpEntityName}DTO = serviceResult.getData();

        ${entityName}AO ao = new ${entityName}AO();
        BeanUtils.copyProperties(${humpEntityName}DTO, ao);

        //ao.setStatus(status.code);
        //ao.setOperateUserId(user.getId());
        //ao.setOperateUserName(user.getName());
        //ao.setOperateTime(new Date());

        ${humpEntityName}GeneratedMapper.updateByPrimaryKeySelective(ao);

        return ServiceResult.SUCCESS(Boolean.TRUE, message);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> update${entityName}(final ${entityName}DTO ${humpEntityName}DTO, final UserInfoDTO user) {
        ServiceResult<${entityName}DTO> serviceResult = get${entityName}(new ${entityName}SearchDTO(${humpEntityName}DTO.getId()), user);
        if (!serviceResult.getSuccess()) {
            return ServiceResult.ERROR(serviceResult.getMessage());
        }

        ValidateResult<Boolean> validateResult = BeanValidateUtil.validate(${humpEntityName}DTO);
        if (!validateResult.getSuccess()) {
            return ServiceResult.ERROR(validateResult.getMessage());
        }

        /**
        * 查询名称是否存在
        */
        //${entityName}Criteria ${humpEntityName}Criteria = new ${entityName}Criteria();
        //${humpEntityName}Criteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andIdNotEqualTo(${humpEntityName}DTO.getId()).andNameEqualTo(${humpEntityName}DTO.getName()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //Long count = ${humpEntityName}GeneratedMapper.countByCriteria(${humpEntityName}Criteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("${schema}名称已经存在, 名称：" + ${humpEntityName}DTO.getName());
        //}

        /**
        * 查询编号是否存在
        */
        //${humpEntityName}Criteria.clear();
        //${humpEntityName}Criteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andIdNotEqualTo(${humpEntityName}DTO.getId()).andCodeEqualTo(${humpEntityName}DTO.getCode()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //count = ${humpEntityName}GeneratedMapper.countByCriteria(${humpEntityName}Criteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("${schema}编号已经存在, 编号：" + ${humpEntityName}DTO.getCode());
        //}

        ${entityName}AO ao = new ${entityName}AO();
        BeanUtils.copyProperties(${humpEntityName}DTO, ao);

        //ao.setOperateUserId(user.getId());
        //ao.setOperateUserName(user.getName());
        //ao.setOperateTime(new Date());

        //ao.setStatus(null);

        ${humpEntityName}GeneratedMapper.updateByPrimaryKeySelective(ao);

        return ServiceResult.SUCCESS(Boolean.TRUE, "修改${schema}成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<String> add${entityName}(final ${entityName}DTO ${humpEntityName}DTO, final UserInfoDTO user) {
        ValidateResult<Boolean> validateResult = BeanValidateUtil.validate(${humpEntityName}DTO);
        if (!validateResult.getSuccess()) {
            return ServiceResult.ERROR(validateResult.getMessage());
        }

        /**
        * 查询名称是否存在
        */
        //${entityName}Criteria ${humpEntityName}Criteria = new ${entityName}Criteria();
        //${humpEntityName}Criteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andNameEqualTo(${humpEntityName}DTO.getName()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //Long count = ${humpEntityName}GeneratedMapper.countByCriteria(${humpEntityName}Criteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("${schema}名称已经存在, 名称：" + ${humpEntityName}DTO.getName());
        //}

        /**
        * 查询编号是否存在
        */
        //${humpEntityName}Criteria.clear();
        //${humpEntityName}Criteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andCodeEqualTo(${humpEntityName}DTO.getCode()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //count = ${humpEntityName}GeneratedMapper.countByCriteria(${humpEntityName}Criteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("${schema}编号已经存在, 编号：" + ${humpEntityName}DTO.getCode());
        //}

        //${humpEntityName}DTO.setStatus(DATA_ENUM.STATUS.ENABLE.code);

        ${entityName}AO ao = new ${entityName}AO();
        BeanUtils.copyProperties(${humpEntityName}DTO, ao);

        //ao.setOrgId(user.getOrgId());

        //ao.setCreateUserId(user.getId());
        //ao.setCreateUserName(user.getName());
        //ao.setCreateTime(new Date());

        ${humpEntityName}GeneratedMapper.insert(ao);

        return ServiceResult.SUCCESS(ao.getId(), "添加${schema}成功");
    }

    @Override
    public ServiceResult<${entityName}DTO> get${entityName}(final ${entityName}SearchDTO searchDTO, final UserInfoDTO user) {
        if (StringUtils.isEmpty(searchDTO.getId())) {
        return ServiceResult.ERROR("ID不能为空");
        }

        ${entityName}AO ao = ${humpEntityName}GeneratedMapper.selectByPrimaryKey(searchDTO.getId());

        if (ao == null) {
            return ServiceResult.ERROR("${schema}不存在");
        }

        ${entityName}DTO ${humpEntityName}DTO = new ${entityName}DTO();
        BeanUtils.copyProperties(ao, ${humpEntityName}DTO);

        return ServiceResult.SUCCESS(${humpEntityName}DTO, "获取信息成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> disable${entityName}(${entityName}SearchDTO searchDTO, final UserInfoDTO user) {
        return update${entityName}Status(searchDTO, user, DATA_ENUM.STATUS.DISABLE, "停用${schema}成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> enable${entityName}(${entityName}SearchDTO searchDTO, final UserInfoDTO user) {
        return update${entityName}Status(searchDTO, user, DATA_ENUM.STATUS.ENABLE, "启用${schema}成功");
    }

    @Override
    public ServiceResult<${entityName}DTO> get${entityName}Detail(${entityName}SearchDTO searchDTO, final UserInfoDTO user) {
        if (StringUtils.isEmpty(searchDTO.getId())) {
            return ServiceResult.ERROR("ID不能为空");
        }

        ${entityName}DTO ${humpEntityName}DTO = ${humpEntityName}CustomizedMapper.select${entityName}DetailBySearchDTO(searchDTO);

        if (${humpEntityName}DTO == null) {
            return ServiceResult.ERROR("${schema}不存在");
        }

        return ServiceResult.SUCCESS(${humpEntityName}DTO, "获取${schema}详情成功");
    }
}
