package com.godboot.app.service.impl;

import com.godboot.app.appobject.AccountAO;
import com.godboot.app.model.dto.AccountDTO;
import com.godboot.framework.constant.DATA_ENUM;
import com.godboot.framework.entity.PageResult;
import com.godboot.framework.entity.ServiceResult;
import com.godboot.framework.entity.SessionUser;
import com.godboot.framework.entity.ValidateResult;
import com.godboot.framework.util.BeanValidateUtil;
import com.godboot.app.mapper.customized.mysql.AccountCustomizedMapper;
import com.godboot.app.mapper.gen.mysql.AccountGeneratedMapper;
import com.godboot.app.model.search.AccountSearchDTO;
import com.godboot.app.service.IAccountService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


/**
 * 应用对象 - AccountServiceImpl.
 * <p>
 * 对象中文名 - 账户.
 * <p>
 * 该类于 2019-12-25 16:41:23 首次生成，后由开发手工维护。
 * </p>
 *
 * @author author
 * @version 1.0.0.0, 十二月 25, 2019
 * @copyright copyright
 * @see AccountServiceImpl
 */
@Service
@Slf4j
public class AccountServiceImpl implements IAccountService {
    @Resource
    private AccountGeneratedMapper accountGeneratedMapper;

    @Resource
    private AccountCustomizedMapper accountCustomizedMapper;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public ServiceResult<PageResult<List<AccountDTO>>> getAccountList(final AccountSearchDTO searchDTO, final SessionUser user) throws InterruptedException {
        AtomicInteger total = new AtomicInteger();
        AtomicReference<List<AccountDTO>> dtoList = new AtomicReference<>();

        CountDownLatch countDownLatch = new CountDownLatch(2);

        threadPoolTaskExecutor.execute(() -> {
            try {
                total.set(getListCount(searchDTO));
            } finally {
                countDownLatch.countDown();
            }
        });

        threadPoolTaskExecutor.execute(() -> {
            try {
                dtoList.set(accountCustomizedMapper.selectAccountListBySearchDTO(searchDTO));
            } finally {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await();

        return ServiceResult.SUCCESS(new PageResult<>(total.get(), dtoList.get()), "获取账户列表成功");
    }

    @Override
    public ServiceResult<Object> test(AccountSearchDTO searchDTO, SessionUser user) {

        Future<List<AccountDTO>> future = threadPoolTaskExecutor.submit(() -> {
            List<AccountDTO> accountDTOS = accountCustomizedMapper.selectAccountListBySearchDTO(searchDTO);
            return accountDTOS;
        });

        return ServiceResult.SUCCESS(future);
    }

    @Override
    public ServiceResult<Integer> getAccountCount(AccountSearchDTO searchDTO, SessionUser user) {
        return ServiceResult.SUCCESS(getListCount(searchDTO), "获取账户数量成功");
    }

    private Integer getListCount(AccountSearchDTO searchDTO) {
        Integer total = accountCustomizedMapper.selectAccountCountBySearchDTO(searchDTO);
        return total;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> deleteAccount(AccountSearchDTO searchDTO, SessionUser user) {
        ServiceResult<AccountDTO> serviceResult = getAccount(searchDTO, user);
        if (!serviceResult.getSuccess() || serviceResult.getData() == null) {
            return ServiceResult.ERROR(serviceResult.getMessage());
        }

        AccountAO ao = new AccountAO();
        ao.setId(searchDTO.getId());

        //ao.setDeleteStatus(DATA_ENUM.BOOL_STATUS.YES.code+"");
        //ao.setOperateUserId(user.getId());
        //ao.setOperateUserName(user.getName());
        //ao.setOperateTime(new Date());

        accountGeneratedMapper.updateByPrimaryKeySelective(ao);

        return ServiceResult.SUCCESS(Boolean.TRUE, "删除账户成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> updateAccountStatus(AccountSearchDTO searchDTO, SessionUser user, DATA_ENUM.STATUS status) {
        return updateAccountStatus(searchDTO, user, status, "修改账户状态成功");
    }

    private ServiceResult<Boolean> updateAccountStatus(final AccountSearchDTO searchDTO, final SessionUser user, final DATA_ENUM.STATUS status, final String message) {
        ServiceResult<AccountDTO> serviceResult = getAccount(searchDTO, user);
        if (!serviceResult.getSuccess() || serviceResult.getData() == null) {
            return ServiceResult.ERROR(serviceResult.getMessage());
        }

        AccountDTO accountDTO = serviceResult.getData();

        AccountAO ao = new AccountAO();
        BeanUtils.copyProperties(accountDTO, ao);

        //ao.setStatus(status.code);
        //ao.setOperateUserId(user.getId());
        //ao.setOperateUserName(user.getName());
        //ao.setOperateTime(new Date());

        accountGeneratedMapper.updateByPrimaryKeySelective(ao);

        return ServiceResult.SUCCESS(Boolean.TRUE, message);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> updateAccount(final AccountDTO accountDTO, final SessionUser user) {
        ServiceResult<AccountDTO> serviceResult = getAccount(new AccountSearchDTO(accountDTO.getId()), user);
        if (!serviceResult.getSuccess()) {
            return ServiceResult.ERROR(serviceResult.getMessage());
        }

        ValidateResult<Boolean> validateResult = BeanValidateUtil.validate(accountDTO);
        if (!validateResult.getSuccess()) {
            return ServiceResult.ERROR(validateResult.getMessage());
        }

        /**
         * 查询名称是否存在
         */
        //AccountCriteria accountCriteria = new AccountCriteria();
        //accountCriteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andIdNotEqualTo(accountDTO.getId()).andNameEqualTo(accountDTO.getName()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //Long count = accountGeneratedMapper.countByCriteria(accountCriteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("账户名称已经存在, 名称：" + accountDTO.getName());
        //}

        /**
         * 查询编号是否存在
         */
        //accountCriteria.clear();
        //accountCriteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andIdNotEqualTo(accountDTO.getId()).andCodeEqualTo(accountDTO.getCode()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //count = accountGeneratedMapper.countByCriteria(accountCriteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("账户编号已经存在, 编号：" + accountDTO.getCode());
        //}

        AccountAO ao = new AccountAO();
        BeanUtils.copyProperties(accountDTO, ao);

        //ao.setOperateUserId(user.getId());
        //ao.setOperateUserName(user.getName());
        //ao.setOperateTime(new Date());

        //ao.setStatus(null);

        accountGeneratedMapper.updateByPrimaryKeySelective(ao);

        return ServiceResult.SUCCESS(Boolean.TRUE, "修改账户成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<String> addAccount(final AccountDTO accountDTO, final SessionUser user) {
        ValidateResult<Boolean> validateResult = BeanValidateUtil.validate(accountDTO);
        if (!validateResult.getSuccess()) {
            return ServiceResult.ERROR(validateResult.getMessage());
        }

        /**
         * 查询名称是否存在
         */
        //AccountCriteria accountCriteria = new AccountCriteria();
        //accountCriteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andNameEqualTo(accountDTO.getName()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //Long count = accountGeneratedMapper.countByCriteria(accountCriteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("账户名称已经存在, 名称：" + accountDTO.getName());
        //}

        /**
         * 查询编号是否存在
         */
        //accountCriteria.clear();
        //accountCriteria.createCriteria().andOrgIdEqualTo(user.getOrgId()).andCodeEqualTo(accountDTO.getCode()).andStatusNotEqualTo(DATA_ENUM.STATUS.DELETE.code);

        //count = accountGeneratedMapper.countByCriteria(accountCriteria);
        //if (count > 0) {
        //  return ServiceResult.ERROR("账户编号已经存在, 编号：" + accountDTO.getCode());
        //}

        //accountDTO.setStatus(DATA_ENUM.STATUS.ENABLE.code);

        AccountAO ao = new AccountAO();
        BeanUtils.copyProperties(accountDTO, ao);

        //ao.setOrgId(user.getOrgId());

        //ao.setCreateUserId(user.getId());
        //ao.setCreateUserName(user.getName());
        //ao.setCreateTime(new Date());

        accountGeneratedMapper.insert(ao);

        return ServiceResult.SUCCESS(ao.getId(), "添加账户成功");
    }

    @Override
    public ServiceResult<AccountDTO> getAccount(final AccountSearchDTO searchDTO, final SessionUser user) {
        if (StringUtils.isEmpty(searchDTO.getId())) {
            return ServiceResult.ERROR("ID不能为空");
        }

        AccountAO ao = accountGeneratedMapper.selectByPrimaryKey(searchDTO.getId());

        if (ao == null) {
            return ServiceResult.ERROR("账户不存在");
        }

        AccountDTO accountDTO = new AccountDTO();
        BeanUtils.copyProperties(ao, accountDTO);

        return ServiceResult.SUCCESS(accountDTO, "获取信息成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> disableAccount(AccountSearchDTO searchDTO, final SessionUser user) {
        return updateAccountStatus(searchDTO, user, DATA_ENUM.STATUS.DISABLE, "停用账户成功");
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ServiceResult<Boolean> enableAccount(AccountSearchDTO searchDTO, final SessionUser user) {
        return updateAccountStatus(searchDTO, user, DATA_ENUM.STATUS.ENABLE, "启用账户成功");
    }

    @Override
    public ServiceResult<AccountDTO> getAccountDetail(AccountSearchDTO searchDTO, final SessionUser user) {
        if (StringUtils.isEmpty(searchDTO.getId())) {
            return ServiceResult.ERROR("ID不能为空");
        }

        AccountDTO accountDTO = accountCustomizedMapper.selectAccountDetailBySearchDTO(searchDTO);

        if (accountDTO == null) {
            return ServiceResult.ERROR("账户不存在");
        }

        return ServiceResult.SUCCESS(accountDTO, "获取账户详情成功");
    }
}
