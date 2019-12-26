package com.godboot.framework.plugin.mybatis;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.JavaVisibility;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;

/**
 * 应用对象 - 序列化生成的模型类以及查询对象类 SerializablePlugin
 * <p>
 * <p>
 * 该类于 2018-04-17 10:52:33 首次生成，后由开发手工维护。
 * </p>
 *
 * @author e-change Co., Ltd.
 * @version 1.0.0.0, 四月 17, 2018
 * @copyright 云南一乘驾驶培训股份有限公司
 */
public class SerializablePlugin extends PluginAdapter {

    private FullyQualifiedJavaType serializable;

    public SerializablePlugin() {
        super();
        serializable = new FullyQualifiedJavaType("java.io.Serializable");
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelExampleClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeSerializable(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeSerializable(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelPrimaryKeyClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeSerializable(topLevelClass, introspectedTable);
        return true;
    }

    @Override
    public boolean modelRecordWithBLOBsClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        makeSerializable(topLevelClass, introspectedTable);
        return true;
    }

    protected void makeSerializable(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        topLevelClass.addImportedType(serializable);
        topLevelClass.addSuperInterface(serializable);
        Field field = new Field();
        field.setFinal(true);
        field.setInitializationString("1L");
        field.setName("serialVersionUID");
        field.setStatic(true);
        field.setType(new FullyQualifiedJavaType("long"));
        field.setVisibility(JavaVisibility.PRIVATE);
        context.getCommentGenerator().addFieldComment(field, introspectedTable);
        topLevelClass.addField(field);
    }
}

