<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${customizedMapperPackage}.${entityName}CustomizedMapper">

    <!--返回ResultMap, 创建日期: ${date}-->
    <resultMap id="BaseResultMap" type="${dtoPackage}.${entityName}DTO" extends="${mapperPackage}.${entityName}GeneratedMapper.BaseResultMap" />

    <!--主表查询字段-->
    <sql id="Base_Column_List">${Base_Column_List}</sql>

    <!--主表查询字段（全部）-->
    <sql id="Base_Column_List_All">t1.*</sql>

    <!--查询列表-->
    <select id="select${entityName}ListBySearchDTO" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM ${tableName} t1
        <include refid="${entityName}BySearchDTO_T1"/>
        <choose>
            <when test="searchDTO.orderByStr != null and searchDTO.orderByStr.length() > 0">
                ORDER BY ${searchDTO.orderByStr}
            </when>
            <otherwise>
                ORDER BY t1.create_time DESC
            </otherwise>
        </choose>
        <if test="searchDTO.page != null">
            limit #{searchDTO.page.begin} , #{searchDTO.page.length}
        </if>
    </select>

    <!--查询数量-->
    <select id="select${entityName}CountBySearchDTO" resultType="int">
        SELECT COUNT(t1.id)
        FROM ${tableName} t1
        <include refid="${entityName}BySearchDTO_COUNT"/>
    </select>

    <!--查询详情-->
    <select id="select${entityName}DetailBySearchDTO" resultMap="BaseResultMap">
        SELECT
        <include refid="Base_Column_List"/>
        FROM ${tableName} t1
        <include refid="${entityName}BySearchDTO_COUNT"/>
    </select>

    <!--查询条件-->
    <sql id="${entityName}BySearchDTO_T1">
        <trim prefix="WHERE" prefixOverrides="AND">
            <include refid="${entityName}BySearchDTO_T1_SQL"/>
        </trim>
    </sql>

    <sql id="${entityName}BySearchDTO_T1_SQL">
        <if test="searchDTO.searchText != null and searchDTO.searchText.length() > 0">
            AND CONCAT(t1.name) LIKE CONCAT(CONCAT('%', #{searchDTO.searchText,jdbcType=VARCHAR}),'%')
        </if>
        <if test="searchDTO.id != null and searchDTO.id.length() > 0">
            AND t1.id = #{searchDTO.id,jdbcType=BIGINT}
        </if>
        <if test="searchDTO.idList != null and searchDTO.idList.size > 0">
            AND t1.id IN
            <foreach collection="searchDTO.idList" item="id" open="(" separator=","  close=")">
            #{id,jdbcType=BIGINT}
            </foreach>
        </if>
        <if test="searchDTO.status != null">
            AND t1.status = #{searchDTO.status,jdbcType=TINYINT}
        </if>
        <if test="searchDTO.statusList != null and searchDTO.statusList.size > 0">
            AND t1.status IN
            <foreach collection="searchDTO.statusList" item="status" open="(" separator=","  close=")">
            #{status,jdbcType=TINYINT}
            </foreach>
        </if>
        <if test="searchDTO.excludeDeleteStatus">
            AND t1.delete_status != #{searchDTO.BOOL_YES,jdbcType=TINYINT}
        </if>
        <if test="searchDTO.enableStatus">
            AND t1.status = #{searchDTO.STATUS_ENABLE,jdbcType=TINYINT}
        </if>
    </sql>

    <sql id="${entityName}BySearchDTO_COUNT">
        <trim prefix="WHERE" prefixOverrides="AND">
            <include refid="${entityName}BySearchDTO_T1_SQL"/>
        </trim>
    </sql>
</mapper>