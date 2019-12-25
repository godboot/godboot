package com.godboot.maven.plugin.modify;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.util.Properties;

/**
 * @author E-CHANGE
 * @date 2018-01-07
 * @goal modify
 */
public class ModifyMojo extends AbstractMojo {
    private MavenProject project;

    private String resourceBaseDir;

    public ModifyMojo() {

    }

    @Override
    public void execute()
            throws MojoExecutionException, MojoFailureException {
        Properties properties = this.project.getProperties();
        if (null != properties) {
            String xmlMapperPackage = properties.getProperty("mybatis.mapperPackage");
            if (null == xmlMapperPackage) {
                return;
            }
            String genxmlMapperPackage = this.resourceBaseDir.concat(xmlMapperPackage.replace(".", File.separator));

            copy(genxmlMapperPackage);

            String customizedXmlMapperPackage = xmlMapperPackage.replace("dao.gen", "dao.customized");
            String customizedFile = this.resourceBaseDir.concat(customizedXmlMapperPackage.replace(".", File.separator));
            copy(customizedFile);
        }
    }

    private void copy(String genFile) {
        File file = new File(genFile);
        if (!file.exists()) {
            return;
        }
        File fileParent = new File(file.getParent());
        if (!fileParent.exists()) {
            return;
        }

        FileUtil.xcopy(file, fileParent);
    }
}
