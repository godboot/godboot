package com.godboot.app.service;

import com.godboot.app.model.dto.AccountDTO;
import com.godboot.app.model.search.AccountSearchDTO;
import com.godboot.framework.constant.DATA_ENUM;
import com.godboot.framework.entity.PageResult;
import com.godboot.framework.entity.ServiceResult;
import com.godboot.framework.entity.SessionUser;

import java.util.List;


/**
 * 应用对象 - IAccountService.
 * <p>
 * 对象中文名 - 账户.
 * <p>
 * 该类于 2019-12-25 16:41:22 首次生成，后由开发手工维护。
 * </p>
 *
 * @author author
 * @version 1.0.0.0, 十二月 25, 2019
 * @copyright copyright
 * @see IAccountService
 */
public interface IAccountService {
    /**
     * 获取账户列表
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<PageResult<List<AccountDTO>>> getAccountList(AccountSearchDTO searchDTO, SessionUser user) throws InterruptedException;

    /**
     * 获取账户列表
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<Object> test(AccountSearchDTO searchDTO, SessionUser user);

    /**
     * 获取账户数量
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<Integer> getAccountCount(AccountSearchDTO searchDTO, SessionUser user);

    /**
     * 删除账户信息
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<Boolean> deleteAccount(AccountSearchDTO searchDTO, SessionUser user);

    /**
     * 修改账户状态
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @param status    修改状态
     * @return
     */
    ServiceResult<Boolean> updateAccountStatus(AccountSearchDTO searchDTO, SessionUser user, DATA_ENUM.STATUS status);

    /**
     * 修改账户信息
     *
     * @param accountDTO 账户信息
     * @param user       当前操作用户
     * @return
     */
    ServiceResult<Boolean> updateAccount(AccountDTO accountDTO, SessionUser user);

    /**
     * 添加账户信息
     *
     * @param accountDTO 账户信息
     * @param user       当前操作用户
     * @return
     */
    ServiceResult<String> addAccount(AccountDTO accountDTO, SessionUser user);

    /**
     * 查询账户信息
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<AccountDTO> getAccount(AccountSearchDTO searchDTO, SessionUser user);

    /**
     * 禁用账户状态
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<Boolean> disableAccount(AccountSearchDTO searchDTO, SessionUser user);

    /**
     * 启用账户状态
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<Boolean> enableAccount(AccountSearchDTO searchDTO, SessionUser user);

    /**
     * 查询账户详情
     *
     * @param searchDTO 查询条件
     * @param user      当前操作用户
     * @return
     */
    ServiceResult<AccountDTO> getAccountDetail(AccountSearchDTO searchDTO, SessionUser user);
}

