package com.godboot.framework.plugin.mybatis;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 应用对象 - Mapper 基类BaseGeneratedMapper.
 * <p>
 * <p>
 * 该类于 2018-04-17 10:52:33 首次生成，后由开发手工维护。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 17, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
public interface BaseGeneratedMapper<EntityAO, EntityCriteria> {
    /**
     * 根据条件统计数量
     *
     * @param entityCriteria 条件
     * @return
     */
    Integer countByCriteria(EntityCriteria entityCriteria);

    /**
     * 根据条件删除数据
     *
     * @param entityCriteria 条件
     * @return
     */
    Integer deleteByCriteria(EntityCriteria entityCriteria);

    /**
     * 根据主键删除数据
     *
     * @param id 主键
     * @return
     */
    Integer deleteByPrimaryKey(String id);

    /**
     * 插入实体对象（包括非空）
     *
     * @param entityAO 数据
     * @return
     */
    Integer insert(EntityAO entityAO);

    /**
     * 插入实体对象（不包括非空）
     *
     * @param entityAO 数据
     * @return
     */
    Integer insertSelective(EntityAO entityAO);

    /**
     * 根据条件查询数据列表
     *
     * @param entityCriteria 条件
     * @return
     */
    List<EntityAO> selectByCriteria(EntityCriteria entityCriteria);

    /**
     * 根据主键查询数据
     *
     * @param id 主键
     * @return
     */
    EntityAO selectByPrimaryKey(String id);

    /**
     * 根据条件更新数据（不包括非空）
     *
     * @param entityAO       数据
     * @param entityCriteria 条件
     * @return
     */
    Integer updateByCriteriaSelective(@Param("record") EntityAO entityAO, @Param("example") EntityCriteria entityCriteria);

    /**
     * 根据条件更新数据（包括非空）
     *
     * @param entityAO       数据
     * @param entityCriteria 条件
     * @return
     */
    Integer updateByCriteria(@Param("record") EntityAO entityAO, @Param("example") EntityCriteria entityCriteria);

    /**
     * 根据主键更新数据（不包括非空）
     *
     * @param entityAO 数据
     * @return
     */
    Integer updateByPrimaryKeySelective(EntityAO entityAO);

    /**
     * 根据主键更新数据（包括非空）
     *
     * @param entityAO 数据
     * @return
     */
    Integer updateByPrimaryKey(EntityAO entityAO);
}
