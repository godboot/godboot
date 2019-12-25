package com.godboot.framework.plugin.mybatis;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This plugin demonstrates overriding the initialized() method to rename the
 * generated example classes. Instead of xxxExample, the classes will be named
 * xxxCriteria
 *
 * This plugin accepts two properties:
 * <ul>
 * <li><tt> searchString</tt> (required) the regular expression of the name
 * search.</li>
 * <li><tt> replaceString </tt> (required) the replacement String.</li>
 * </ul>
 *
 * For example, to change the name of the generated Example classes from
 * xxxExample to xxxCriteria, specify the following:
 *
 * <dl>
 * <dt>searchString</dt>
 * <dd>Example$</dd>
 * <dt>replaceString</dt>
 * <dd>Criteria</dd>
 * </dl>
 *
 *
 * @author Jeff Butler
 * @author <a mailto="ch_g2018@163.com">GUO CHA</a>
 * @version 1.0.0.0, 2016/6/2
 */
public final class RenameExampleClassPlugin extends PluginAdapter {

    private String searchString;

    private String replaceString;

    private Pattern pattern;

    public RenameExampleClassPlugin() {
    }

    @Override
    public boolean validate(List<String> warnings) {

        this.searchString = this.properties.getProperty("searchString");
        this.replaceString = this.properties.getProperty("replaceString");
        boolean valid = StringUtility.stringHasValue(this.searchString) && StringUtility.stringHasValue(this.replaceString);
        if (valid) {
            this.pattern = Pattern.compile(this.searchString);
        } else {
            if (!StringUtility.stringHasValue(this.searchString)) {
                warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "searchString"));
            }

            if (!StringUtility.stringHasValue(this.replaceString)) {
                warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "replaceString"));
            }
        }

        return valid;
    }

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        String type = introspectedTable.getExampleType();
        Matcher matcher = pattern.matcher(type);
        type = matcher.replaceAll( replaceString);
        introspectedTable.setExampleType(type) ;
        introspectedTable.setCountByExampleStatementId( "countBy" + replaceString );
        introspectedTable.setDeleteByExampleStatementId( "deleteBy" + replaceString );
        introspectedTable.setSelectByExampleStatementId( "selectBy" + replaceString );
        introspectedTable.setSelectByExampleWithBLOBsStatementId( "selectBy" + replaceString + "WithBLOBs");
        introspectedTable.setUpdateByExampleStatementId( "updateBy" + replaceString );
        introspectedTable.setUpdateByExampleSelectiveStatementId( "updateBy" + replaceString + "Selective");
        introspectedTable.setUpdateByExampleWithBLOBsStatementId( "updateBy" + replaceString + "WithBLOBs");
        introspectedTable.setExampleWhereClauseId( replaceString + "_Where_Clause" );
        introspectedTable.setMyBatis3UpdateByExampleWhereClauseId( "Update_By_" + replaceString + "_Where_Clause");
    }
}

