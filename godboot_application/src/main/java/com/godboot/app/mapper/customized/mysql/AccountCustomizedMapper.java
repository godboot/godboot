package com.godboot.app.mapper.customized.mysql;

import com.godboot.app.model.dto.AccountDTO;
import com.godboot.app.model.search.AccountSearchDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

/**
 * 应用对象 - AccountCustomizedMapper.
 * <p>
 * 对象中文名 - 账户.
 * <p>
 * 该类于 2019-12-25 15:41:54 首次生成，后由开发手工维护。
 * </p>
 *
 * @see       AccountCustomizedMapper
 * @author    author
 * @version   1.0.0.0, 十二月 25, 2019
 * @copyright copyright
 */
@Mapper
public interface AccountCustomizedMapper {
    /**
    * 根据searchDTO查询账户列表
    *
    * @param searchDTO 查询条件
    * @return
    */
    List<AccountDTO> selectAccountListBySearchDTO(@Param("searchDTO") AccountSearchDTO searchDTO);

    /**
    * 根据searchDTO查询账户数量
    *
    * @param searchDTO 查询条件
    * @return
    */
    Integer selectAccountCountBySearchDTO(@Param("searchDTO") AccountSearchDTO searchDTO);

    /**
    * 根据searchDTO查询账户详情
    *
    * @param searchDTO 查询条件
    * @return
    */
    AccountDTO selectAccountDetailBySearchDTO(@Param("searchDTO") AccountSearchDTO searchDTO);
}
