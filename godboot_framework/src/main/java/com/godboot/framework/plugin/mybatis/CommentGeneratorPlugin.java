package com.godboot.framework.plugin.mybatis;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.DefaultCommentGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 应用对象 - 实体类注释 插件CommentGeneratorPlugin.
 * <p>
 * <p>
 * 该类于 2018-04-17 10:52:33 首次生成，后由开发手工维护。
 * </p>
 *
 * @author 路德维希
 * @version 1.0.0.0, 四月 17, 2018
 * @copyright goddoitnow@gmail.com
 */
public class CommentGeneratorPlugin extends DefaultCommentGenerator {
    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        super.addFieldComment(field, introspectedTable, introspectedColumn);
        if (introspectedColumn.getRemarks() != null && !introspectedColumn.getRemarks().equals("")) {
            field.addJavaDocLine("/**");
            field.addJavaDocLine(" * " + introspectedColumn.getRemarks());
            field.addJavaDocLine(" */");
            field.addAnnotation("@ApiModelProperty(value = \"" + introspectedColumn.getRemarks() + "\")");
        }
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiModelProperty"));
        topLevelClass.addImportedType(new FullyQualifiedJavaType("io.swagger.annotations.ApiModel"));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        topLevelClass.addAnnotation("@ApiModel(value = \"" + introspectedTable.getTableConfiguration().getDomainObjectName() + "\")");
        topLevelClass.addJavaDocLine("/**");
        topLevelClass.addJavaDocLine(" * " + "应用对象 - " + introspectedTable.getTableConfiguration().getDomainObjectName() + ".");
        topLevelClass.addJavaDocLine(" * " + "<p>");
        topLevelClass.addJavaDocLine(" * " + "对象中文名 - " + (StringUtils.isEmpty(introspectedTable.getTableConfiguration().getSchema()) ? "数据" : introspectedTable.getTableConfiguration().getSchema()) + ".");
        topLevelClass.addJavaDocLine(" * " + "<p>");
        topLevelClass.addJavaDocLine(" * " + "该类于 " + sdf.format(new Date()) + " 首次生成，后由开发手工维护。");
        topLevelClass.addJavaDocLine(" * " + "</p>");
        topLevelClass.addJavaDocLine(" * " + "");
        topLevelClass.addJavaDocLine(" * " + "@author    路德维希");
        topLevelClass.addJavaDocLine(" * " + "@version   1.0, " + sdf.format(new Date()));
        topLevelClass.addJavaDocLine(" * " + "@copyright goddoitnow@gmail.com");
        topLevelClass.addJavaDocLine(" */");

        super.addModelClassComment(topLevelClass, introspectedTable);
    }
}
