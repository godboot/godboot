package com.godboot.maven.plugin.gen;

import com.godboot.maven.plugin.modify.FileUtil;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.ShellCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.ClassloaderUtility;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;
import org.mybatis.generator.maven.MavenProgressCallback;
import org.mybatis.generator.maven.MavenShellCallback;
import org.mybatis.generator.maven.MyBatisGeneratorMojo;
import org.mybatis.generator.maven.SqlScriptRunner;

import java.io.*;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 应用对象 - GenMojo.
 * <p>
 * <p>
 * 该类于 2018-02-09 09:51:48 首次生成，后由开发手工维护。
 * </p>
 *
 * @author http://www.nessen.com.cn
 * @version 1.0.0.0, 二月 09, 2018
 * @copyright 上海立深行国际贸易有限公司
 */
@Mojo(name = "gen", defaultPhase = LifecyclePhase.PROCESS_RESOURCES)
public class GenMojo extends MyBatisGeneratorMojo {

    private ThreadLocal<ClassLoader> savedClassloader = new ThreadLocal<>();

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private List<TableConfiguration> tableConfigurationList;

    @Parameter(property = "gen.javaBaseDir", defaultValue = "${project.basedir}/src/main/java/")
    private String javaBaseDir;

    @Parameter(property = "gen.resourceBaseDir", defaultValue = "${project.basedir}/src/main/resources/")
    private String resourceBaseDir;

    @Parameter(property = "gen.webappBaseDir", defaultValue = "${project.basedir}/src/main/webapp/")
    private String webappBaseDir;

    @Parameter(property = "gen.entityPackage")
    private String entityPackage;

    @Parameter(property = "gen.mapperPackage")
    private String mapperPackage;

    @Parameter(property = "gen.appObjectPackage")
    private String appObjectPackage;

    @Parameter(property = "gen.dtoPackage")
    private String dtoPackage;

    @Parameter(property = "gen.searchDTOPackage")
    private String searchDTOPackage;

    @Parameter(property = "gen.servicePackage")
    private String servicePackage;

    @Parameter(property = "gen.customizedMapperPackage")
    private String customizedMapperPackage;

    @Parameter(property = "gen.dubboConfigPackage")
    private String dubboConfigPackage;

    @Parameter(property = "gen.controllerPackage")
    private String controllerPackage;

    @Parameter(property = "gen.whetherGenDTO")
    private boolean whetherGenDTO = false;

    @Parameter(property = "gen.whetherGenSearchDTO")
    private boolean whetherGenSearchDTO = false;

    @Parameter(property = "gen.whetherGenService")
    private boolean whetherGenService = false;

    @Parameter(property = "gen.whetherGenCustomizedMapper")
    private boolean whetherGenCustomizedMapper = false;

    @Parameter(property = "gen.whetherGenController")
    private boolean whetherGenController = false;

    @Parameter(property = "gen.excludeGenServiceEntity")
    private String excludeGenServiceEntity;

    @Parameter(property = "gen.genControllerEntity")
    private String genControllerEntity;

    @Parameter(property = "gen.currentArtifactId")
    private String currentArtifactId;

    @Parameter(property = "gen.targetArtifactId")
    private String targetArtifactId;

    @Parameter(property = "gen.controllerTargetPath")
    private String controllerTargetPath;

    @Parameter(property = "gen.dubboConsumerPath")
    private String dubboConsumerPath;

    private List excludeGenServiceEntityList = new ArrayList();

    private List genControllerEntityList = new ArrayList();

    private Map<String, String> generatorMapperXmlContent = new HashMap<>(16);

    private static final freemarker.template.Configuration TEMPLATE_CFG = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);

    @Parameter(property = "project", required = true, readonly = true)
    private MavenProject project;

    @Parameter(property = "mybatis.generator.outputDirectory", defaultValue = "${project.build.directory}/generated-sources/mybatis-generator")
    private File outputDirectory;

    @Parameter(property = "mybatis.generator.configurationFile", defaultValue = "${project.basedir}/src/main/resources/mybatis-generator/generatorConfig-mysql.xml")
    private File configurationFile;

    @Parameter(property = "mybatis.generator.verbose", defaultValue = "false")
    private boolean verbose;

    @Parameter(property = "mybatis.generator.overwrite", defaultValue = "true")
    private boolean overwrite;

    @Parameter(property = "mybatis.generator.sqlScript")
    private String sqlScript;

    @Parameter(property = "mybatis.generator.jdbcDriver")
    private String jdbcDriver;

    @Parameter(property = "mybatis.generator.jdbcURL")
    private String jdbcURL;

    @Parameter(property = "mybatis.generator.jdbcUserId")
    private String jdbcUserId;

    @Parameter(property = "mybatis.generator.jdbcPassword")
    private String jdbcPassword;

    @Parameter(property = "mybatis.generator.tableNames")
    private String tableNames;

    @Parameter(property = "mybatis.generator.contexts")
    private String contexts;

    @Parameter(property = "mybatis.generator.domainObjectNames")
    private String domainObjectNames;

    @Parameter(property = "mybatis.generator.includeCompileDependencies", defaultValue = "false")
    private boolean includeCompileDependencies;

    @Parameter(property = "mybatis.generator.includeAllDependencies", defaultValue = "false")
    private boolean includeAllDependencies;

    @Parameter(property = "gen.copyright", defaultValue = "copyright")
    private String copyright;

    @Parameter(property = "gen.author", defaultValue = "author")
    private String author;

    @Override
    public void execute() throws MojoExecutionException {
        Log log = getLog();

        saveClassLoader();
        calculateClassPath();
        List<Resource> resources = project.getResources();
        List<String> resourceDirectories = new ArrayList<>();
        for (Resource resource : resources) {
            resourceDirectories.add(resource.getDirectory());
        }
        ClassLoader cl = ClassloaderUtility.getCustomClassloader(resourceDirectories);
        ObjectFactory.addExternalClassLoader(cl);
        ObjectFactory.addResourceClassLoader(cl);

        if (this.configurationFile == null) {
            log.info(new MojoExecutionException(Messages.getString("RuntimeError.0")));
            throw new MojoExecutionException(Messages.getString("RuntimeError.0"));
        }

        List warnings = new ArrayList();

        if (!this.configurationFile.exists()) {
            log.info(new MojoExecutionException(Messages.getString("RuntimeError.1", this.configurationFile.toString())));
            throw new MojoExecutionException(Messages.getString("RuntimeError.1", this.configurationFile.toString()));
        }

        runScriptIfNecessary();

        Set fullyqualifiedTables = new HashSet();
        if (StringUtility.stringHasValue(this.tableNames)) {
            StringTokenizer st = new StringTokenizer(this.tableNames, ",");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    fullyqualifiedTables.add(s);
                }
            }
        }

        Set contextsToRun = new HashSet();
        if (StringUtility.stringHasValue(this.contexts)) {
            StringTokenizer st = new StringTokenizer(this.contexts, ",");
            while (st.hasMoreTokens()) {
                String s = st.nextToken().trim();
                if (s.length() > 0) {
                    contextsToRun.add(s);
                }
            }
        }

        try {
            ConfigurationParser cp = new ConfigurationParser(this.project.getProperties(), warnings);
            Configuration config = cp.parseConfiguration(this.configurationFile);
            Context context = config.getContexts().get(0);
            tableConfigurationList = context.getTableConfigurations();

            ShellCallback callback = new MavenShellCallback(this, this.overwrite);
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            myBatisGenerator.generate(new MavenProgressCallback(getLog(), this.verbose), contextsToRun, fullyqualifiedTables);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MojoExecutionException(e.getMessage());
        }

        for (Object error : warnings) {
            getLog().warn(error.toString());
        }

        if ((this.project != null) && (this.outputDirectory != null) && (this.outputDirectory.exists())) {
            this.project.addCompileSourceRoot(this.outputDirectory.getAbsolutePath());
            Resource resource = new Resource();
            resource.setDirectory(this.outputDirectory.getAbsolutePath());
            resource.addInclude("**/*.xml");
            this.project.addResource(resource);
        }

        String currentYear = String.format("%tY", new Object[]{new Date()});
        String copyrightYear = "2018".equals(currentYear) ? currentYear : new StringBuilder().append("2018-").append(currentYear).toString();

        StringBuilder startBuilder = new StringBuilder("-----------------------------------------------------------------------");

        if (copyrightYear.length() > 4) {
            startBuilder.append("*****");
        }

        log.info(startBuilder.toString());
        log.info("应用基础代码生成工具");
        log.info("生成完成后请");
        log.info("1.查看生成日志输出");
        log.info("2.检查生成的代码是否满足");
        log.info("3.确认无误后再提交代码");
        log.info(new StringBuilder().append("Copyright (C) ").append(copyrightYear).append(", " + copyright + "").toString());
        log.info(startBuilder.toString());

        this.javaBaseDir = this.javaBaseDir.replace("\\", "/");
        this.resourceBaseDir = this.resourceBaseDir.replace("\\", "/");

        log.info("----------------------- 开始生成应用实现基础代码 -------------------------");
        log.info(LINE_SEPARATOR);

        try {
            Set<String> entityNames = getEntityNames();
            Map<String, String> tableNames = getTableNames();

            genAOs(entityNames);
            if (whetherGenDTO) {
                genDTOs(entityNames);
            }
            if (whetherGenSearchDTO) {
                genSearchDTOs(entityNames);
            }
            genMapperXmls(entityNames);
            genMappers(entityNames);
            if (whetherGenService) {
                excludeGenServiceEntityList();
                genService(entityNames);
                genServiceImpl(entityNames);
//                genDubboServiceXml(entityNames);
//                genDubboProviderXml(entityNames);
            }
            if (whetherGenCustomizedMapper) {
                genCustomizedMapper(entityNames);
                genCustomizedMapperXml(entityNames, tableNames);
            }

            if (whetherGenController) {
                genControllerEntityList();
                genController(entityNames);
            }

            log.info(LINE_SEPARATOR);
            log.info("----------------------- 应用实现基础代码生成结束 --------------------------");

        } catch (Exception e) {
            getLog().error("应用实现基础代码生成失败", e);
        }
    }

    private void excludeGenServiceEntityList() {
        if (StringUtils.isBlank(this.excludeGenServiceEntity)) {
            return;
        }

        String[] array = this.excludeGenServiceEntity.split(",");
        excludeGenServiceEntityList = Arrays.asList(array);
    }

    private void genControllerEntityList() {
        if (StringUtils.isBlank(this.genControllerEntity)) {
            return;
        }

        String[] array = this.genControllerEntity.split(",");
        genControllerEntityList = Arrays.asList(array);
    }

    private Set<String> getEntityNames() {
        if (tableConfigurationList == null || tableConfigurationList.size() == 0) {
            getLog().info("");
            return null;
        }

        Set<String> ret = new HashSet<>();
        for (TableConfiguration tableConfiguration : tableConfigurationList) {
            ret.add(tableConfiguration.getDomainObjectName());
        }
        return ret;
    }

    private Map<String, String> getTableNames() {
        if (tableConfigurationList == null || tableConfigurationList.size() == 0) {
            getLog().info("");
            return null;
        }

        Map<String, String> ret = new HashMap<>(16);
        for (TableConfiguration tableConfiguration : tableConfigurationList) {
            ret.put(tableConfiguration.getDomainObjectName(), tableConfiguration.getTableName());
        }
        return ret;
    }

    private Map<String, String> getTableSchema() {
        Map<String, String> ret = new HashMap<>(16);

        if (tableConfigurationList == null || tableConfigurationList.size() == 0) {
            getLog().info("");
            return ret;
        }

        for (TableConfiguration tableConfiguration : tableConfigurationList) {
            ret.put(tableConfiguration.getDomainObjectName(), tableConfiguration.getSchema());
        }
        return ret;
    }

    private String getSchemaByDomainObjectName(String domainObjectName) {
        Map<String, String> tableSchema = getTableSchema();

        String schema = tableSchema.get(domainObjectName);

        return StringUtils.isEmpty(schema) ? "数据" : schema;
    }

    private void genAOs(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            TEMPLATE_CFG.setDefaultEncoding("UTF-8");
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(new StringBuilder().append(templateDir.getPath()).append(File.separator).append("template").toString()));

            Template template = TEMPLATE_CFG.getTemplate("ao.ftl");
            template.setOutputEncoding("UTF-8");

            String appObjectPath = new StringBuilder().append(this.javaBaseDir).append(this.appObjectPackage.replace(".", "/")).toString();
            File appObjectpackageFile = new File(appObjectPath);
            if (!appObjectpackageFile.exists()) {
                appObjectpackageFile.mkdirs();
            }

            for (String entityName : entityNames) {
                File target = new File(new StringBuilder().append(appObjectPath).append(File.separator).append(entityName).append("AO.java").toString());
                if (target.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.appObjectPackage).append('.').append(entityName).append("AO").append("]已经存在，不重复生成").toString());
                    continue;
                }

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
                Map dataModel = new HashMap(16);
                Date date = new Date();
                dataModel.put("appObjectPackage", this.appObjectPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));
                dataModel.put("entityPackage", this.entityPackage);
                dataModel.put("entityName", entityName);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                template.process(dataModel, out);

                if (out != null) {
                    out.flush();
                    out.close();
                }

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.appObjectPackage).append('.').append(entityName).append("AO").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info(e.getMessage());
        }
    }

    private void genMappers(Set<String> entityNames) {
        try {
            Template template = TEMPLATE_CFG.getTemplate("generatedMapper.ftl");
            template.setOutputEncoding("UTF-8");

            String mapperPath = new StringBuilder().append(this.javaBaseDir).append(this.mapperPackage.replace(".", "/")).toString();
            String mapperXmlPath = new StringBuilder().append(this.resourceBaseDir).append(this.mapperPackage.replace(".", "/")).toString();
            for (String entityName : entityNames) {
                File old = new File(new StringBuilder().append(mapperPath).append(File.separator).append(entityName).append("Mapper.java").toString());
                if (!old.exists()) {
                    continue;
                }

                File oldMapperXML = new File(new StringBuilder().append(mapperXmlPath).append(File.separator).append(entityName).append("GeneratedMapper.xml").toString());
                InputStream oldInputStream = new FileInputStream(oldMapperXML);
                String content = IOUtils.toString(oldInputStream, "UTF-8");

                if (oldInputStream != null) {
                    oldInputStream.close();
                }

                File target = new File(new StringBuilder().append(mapperPath).append(File.separator).append(entityName).append("GeneratedMapper.java").toString());
                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
                Map dataModel = new HashMap(16);
                Date date = new Date();

                dataModel.put("appObjectPackage", this.appObjectPackage);
                dataModel.put("entityPackage", this.entityPackage);
                dataModel.put("mapperPackage", this.mapperPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));

                String temp = entityName.concat("WithBLOBs");
                String entityNameWithBLOBsAO = entityName;
                String baseGeneratedMapper = "BaseGeneratedMapper";

                if (content.contains("WithBLOBs")) {
                    baseGeneratedMapper = "BaseWithBLOBsGeneratedMapper";
                }
                if (entityNames.contains(temp)) {
                    entityNameWithBLOBsAO = temp;
                    baseGeneratedMapper = "BaseWithBLOBsGeneratedMapper";
                }

                dataModel.put("baseGeneratedMapper", baseGeneratedMapper);
                dataModel.put("entityNameWithBLOBsAO", entityNameWithBLOBsAO);
                dataModel.put("entityName", entityName);

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                template.process(dataModel, out);

                if (out != null) {
                    out.flush();
                    out.close();
                }

                old.delete();

                getLog().info(new StringBuilder().append("生成DAO[").append(this.appObjectPackage).append('.').append(entityName).append("GeneratedMapper").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info(e.getMessage());
        }
    }

    private void genMapperXmls(Set<String> entityNames) {
        try {
            generatorMapperXmlContent.clear();

            String mapperXmlPath = new StringBuilder().append(this.resourceBaseDir).append(this.mapperPackage.replace(".", "/")).toString();

            for (String entityName : entityNames) {
                File old = new File(new StringBuilder().append(mapperXmlPath).append(File.separator).append(entityName).append("Mapper.xml").toString());

                if (!old.exists()) {
                    continue;
                }
                File target = new File(new StringBuilder().append(mapperXmlPath).append(File.separator).append(entityName).append("GeneratedMapper.xml").toString());

                Date date = new Date();
                StringBuilder contentBuilder = new StringBuilder();

                InputStream oldInputStream = new FileInputStream(old);
                String content = IOUtils.toString(oldInputStream, "UTF-8");

                if (oldInputStream != null) {
                    oldInputStream.close();
                }

                contentBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append(LINE_SEPARATOR).append("<!--").append(LINE_SEPARATOR).append(LINE_SEPARATOR).append("    ").append("Description: ").append("A generated data access implementation of entity ").append(entityName).append(".").append(LINE_SEPARATOR).append("    ").append("             Generated at ").append(this.dateFormat.format(date)).append(", do NOT modify!").append(LINE_SEPARATOR).append("    ").append("Author: <a href=\"mailto:" + author + "/\">" + copyright + "</a>").append(LINE_SEPARATOR).append("    ").append("Version: 1.0.0.0, ").append(String.format(Locale.US, "%1$tb %2$td, %3$tY", new Object[]{date, date, date})).append(LINE_SEPARATOR).append(LINE_SEPARATOR).append("-->");

                content = content.replace("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>", "");
                content = content.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");

                content = content.replace(new StringBuilder().append("\"").append(this.entityPackage).append('.').append(entityName).append("\"").toString(), new StringBuilder().append("\"").append(this.appObjectPackage).append('.').append(entityName).append("AO").append("\"").toString()).replace(new StringBuilder().append("\"").append(this.entityPackage).append('.').append(entityName.concat("WithBLOBs")).append("\"").toString(), new StringBuilder().append("\"").append(this.appObjectPackage).append('.').append(entityName.concat("WithBLOBs")).append("AO").append("\"").toString()).replace(new StringBuilder().append("\"").append(this.mapperPackage).append('.').append(entityName).append("Mapper").append("\"").toString(), new StringBuilder().append("\"").append(this.mapperPackage).append('.').append(entityName).append("GeneratedMapper").append("\"").toString());

                contentBuilder.append(content);

                FileWriter writer = new FileWriter(target);
                IOUtils.write(contentBuilder.toString(), writer);

                if (writer != null) {
                    writer.close();
                }

                generatorMapperXmlContent.put(entityName, contentBuilder.toString());

                old.delete();
            }
        } catch (Exception e) {
            getLog().info("genMapperXmls", e);
        }
    }

    private void genDTOs(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            TEMPLATE_CFG.setDefaultEncoding("UTF-8");
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(new StringBuilder().append(templateDir.getPath()).append(File.separator).append("template").toString()));

            Template template = TEMPLATE_CFG.getTemplate("dto.ftl");
            template.setOutputEncoding("UTF-8");

            /**
             * DTO对象
             */
            String dtoPath = new StringBuilder().append(this.javaBaseDir).append(this.dtoPackage.replace(".", "/")).toString();
            dtoPath = dtoPath.replace(currentArtifactId, targetArtifactId);
            File dtoPackageFile = new File(dtoPath);
            if (!dtoPackageFile.exists()) {
                dtoPackageFile.mkdirs();
            }

            /**
             * entity对象路径
             */
            String entityPath = new StringBuilder().append(this.javaBaseDir).append(this.entityPackage.replace(".", "/")).toString();
            for (String entityName : entityNames) {
                /**
                 * 判断ao对象是否存在
                 */
                File dtoObject = new File(new StringBuilder().append(dtoPath).append(File.separator).append(entityName).append("DTO.java").toString());
                if (dtoObject.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.dtoPackage).append('.').append(entityName).append("DTO").append("]已经存在，不重复生成").toString());
                    continue;
                }

                File entityObject = new File(new StringBuilder().append(entityPath).append(File.separator).append(entityName).append(".java").toString());
                InputStream oldInputStream = new FileInputStream(entityObject);
                String content = IOUtils.toString(oldInputStream, "UTF-8");
                String entityContent = content.substring(content.indexOf("{") + 1, content.indexOf("private static final long serialVersionUID = 1L;"));
                String importContent = content.substring(content.indexOf("import"), content.indexOf("/**"));

                importContent = StringUtils.replace(importContent, "import io.swagger.annotations.ApiModel;", "");

                if (oldInputStream != null) {
                    oldInputStream.close();
                }

                FileOutputStream fileOutputStream = new FileOutputStream(dtoObject);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, "UTF-8");
                Writer out = new BufferedWriter(outputStreamWriter);
                Map dataModel = new HashMap(16);
                Date date = new Date();

                dataModel.put("dtoPackage", this.dtoPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));
                dataModel.put("entityName", entityName.trim());
                dataModel.put("entityContent", entityContent.trim());
                dataModel.put("importContent", importContent.trim());

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                template.process(dataModel, out);

                if (out != null) {
                    out.flush();
                    out.close();
                }

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.dtoPackage).append('.').append(entityName).append("DTO").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info("genDTOs", e);
        }
    }

    private void genSearchDTOs(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            TEMPLATE_CFG.setDefaultEncoding("UTF-8");
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(new StringBuilder().append(templateDir.getPath()).append(File.separator).append("template").toString()));

            Template template = TEMPLATE_CFG.getTemplate("searchDTO.ftl");
            template.setOutputEncoding("UTF-8");

            String searchDTOPath = new StringBuilder().append(this.javaBaseDir).append(this.searchDTOPackage.replace(".", "/")).toString();
            searchDTOPath = searchDTOPath.replace(currentArtifactId, targetArtifactId);
            File searchDTFile = new File(searchDTOPath);
            if (!searchDTFile.exists()) {
                searchDTFile.mkdirs();
            }

            for (String entityName : entityNames) {
                if (excludeGenServiceEntityList.contains(entityName)) {
                    /**
                     * 不生成service的entity跳过
                     */
                    getLog().info(entityName + "不生成search");
                    continue;
                }

                File target = new File(new StringBuilder().append(searchDTOPath).append(File.separator).append(entityName).append("SearchDTO.java").toString());
                if (target.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.searchDTOPackage).append('.').append(entityName).append("SearchDTO").append("]已经存在，不重复生成").toString());
                    continue;
                }

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
                Map dataModel = new HashMap(16);
                Date date = new Date();
                dataModel.put("searchDTOPackage", this.searchDTOPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));
                dataModel.put("entityName", entityName);

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                template.process(dataModel, out);

                out.flush();
                out.close();

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.searchDTOPackage).append('.').append(entityName).append("SearchDTO").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info("genSearchDTOs", e);
        }
    }

    private void genService(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            TEMPLATE_CFG.setDefaultEncoding("UTF-8");
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(new StringBuilder().append(templateDir.getPath()).append(File.separator).append("template").toString()));

            Template template = TEMPLATE_CFG.getTemplate("service.ftl");
            template.setOutputEncoding("UTF-8");

            String servicePath = new StringBuilder().append(this.javaBaseDir).append(this.servicePackage.replace(".", "/")).toString();
            servicePath = servicePath.replace(currentArtifactId, targetArtifactId);
            File serviceFile = new File(servicePath);
            if (!serviceFile.exists()) {
                serviceFile.mkdirs();
            }

            for (String entityName : entityNames) {
                if (excludeGenServiceEntityList.contains(entityName)) {
                    /**
                     * 不生成service的entity跳过
                     */
                    getLog().info(entityName + "不生成service");
                    continue;
                }

                File target = new File(new StringBuilder().append(servicePath).append(File.separator).append("I").append(entityName).append("Service.java").toString());
                if (target.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.servicePackage).append('.').append("I").append(entityName).append("Service").append("]已经存在，不重复生成").toString());
                    continue;
                }

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
                Map dataModel = new HashMap(16);
                Date date = new Date();
                dataModel.put("servicePackage", this.servicePackage);
                dataModel.put("dtoPackage", this.dtoPackage);
                dataModel.put("searchDTOPackage", this.searchDTOPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));
                dataModel.put("entityName", entityName);
                String humpEntityName = StringUtils.lowerCase(entityName.substring(0, 1)) + entityName.substring(1, entityName.length());
                dataModel.put("humpEntityName", humpEntityName);

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                template.process(dataModel, out);

                out.flush();
                out.close();

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.servicePackage).append('.').append("I").append(entityName).append("Service").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info("genService", e);
        }
    }

    private void genServiceImpl(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            TEMPLATE_CFG.setDefaultEncoding("UTF-8");
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(new StringBuilder().append(templateDir.getPath()).append(File.separator).append("template").toString()));

            Template template = TEMPLATE_CFG.getTemplate("serviceImpl.ftl");
            template.setOutputEncoding("UTF-8");

            String servicePath = new StringBuilder().append(this.javaBaseDir).append(this.servicePackage.replace(".", "/")).toString();
            File serviceFile = new File(servicePath);
            if (!serviceFile.exists()) {
                serviceFile.mkdirs();
            }

            for (String entityName : entityNames) {
                if (excludeGenServiceEntityList.contains(entityName)) {
                    /**
                     * 不生成service的entity跳过
                     */
                    getLog().info(entityName + "不生成service");
                    continue;
                }

                File target = new File(new StringBuilder().append(servicePath+"/impl").append(File.separator).append(entityName).append("ServiceImpl.java").toString());
                if (target.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.servicePackage).append('.').append(entityName).append("ServiceImpl").append("]已经存在，不重复生成").toString());
                    continue;
                }

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
                Map dataModel = new HashMap(16);
                Date date = new Date();
                dataModel.put("servicePackage", this.servicePackage);
                dataModel.put("dtoPackage", this.dtoPackage);
                dataModel.put("searchDTOPackage", this.searchDTOPackage);
                dataModel.put("customizedMapperPackage", this.customizedMapperPackage);
                dataModel.put("mapperPackage", this.mapperPackage);
                dataModel.put("appObjectPackage", this.appObjectPackage);
                dataModel.put("entityPackage", this.entityPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));
                dataModel.put("entityName", entityName);
                String humpEntityName = StringUtils.lowerCase(entityName.substring(0, 1)) + entityName.substring(1);
                dataModel.put("humpEntityName", humpEntityName);

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                template.process(dataModel, out);

                out.flush();
                out.close();

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.servicePackage).append('.').append(entityName).append("ServiceImpl").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info("genServiceImpl", e);
        }
    }

    private void genCustomizedMapper(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            TEMPLATE_CFG.setDefaultEncoding("UTF-8");
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(new StringBuilder().append(templateDir.getPath()).append(File.separator).append("template").toString()));

            Template template = TEMPLATE_CFG.getTemplate("customizedMapper.ftl");
            template.setOutputEncoding("UTF-8");

            String customizedMapperPath = new StringBuilder().append(this.javaBaseDir).append(this.customizedMapperPackage.replace(".", "/")).toString();
            File customizedFile = new File(customizedMapperPath);
            if (!customizedFile.exists()) {
                customizedFile.mkdirs();
            }

            for (String entityName : entityNames) {
                if (excludeGenServiceEntityList.contains(entityName)) {
                    /**
                     * 不生成service的entity跳过
                     */
                    getLog().info(entityName + "不生成customizedMapper");
                    continue;
                }

                File target = new File(new StringBuilder().append(customizedMapperPath).append(File.separator).append(entityName).append("CustomizedMapper.java").toString());
                if (target.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.customizedMapperPackage).append('.').append(entityName).append("CustomizedMapper").append("]已经存在，不重复生成").toString());
                    continue;
                }

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
                Map dataModel = new HashMap(16);
                Date date = new Date();

                dataModel.put("dtoPackage", this.dtoPackage);
                dataModel.put("searchDTOPackage", this.searchDTOPackage);
                dataModel.put("customizedMapperPackage", this.customizedMapperPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));
                dataModel.put("entityName", entityName);

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                template.process(dataModel, out);

                out.flush();
                out.close();

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.customizedMapperPackage).append('.').append(entityName).append("CustomizedMapper").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info("genCustomizedMapper", e);
        }
    }

    private void genCustomizedMapperXml(Set<String> entityNames, Map<String, String> tableNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());
            InputStream oldInputStream = new FileInputStream(templateDir + "/template/customizedMapperXml.ftl");
            String content = IOUtils.toString(oldInputStream, "UTF-8");

            if (oldInputStream != null) {
                oldInputStream.close();
            }

            String customizedMapperPath = new StringBuilder().append(this.resourceBaseDir).append(this.customizedMapperPackage.replace(".", "/")).toString();
            File customizedFile = new File(customizedMapperPath);
            if (!customizedFile.exists()) {
                customizedFile.mkdirs();
            }

            for (String entityName : entityNames) {
                if (excludeGenServiceEntityList.contains(entityName)) {
                    /**
                     * 不生成service的entity跳过
                     */
                    getLog().info(entityName + "不生成CustomizedMapperXml");
                    continue;
                }

                File target = new File(new StringBuilder().append(customizedMapperPath).append(File.separator).append(entityName).append("CustomizedMapper.xml").toString());
                if (target.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.customizedMapperPackage).append('.').append(entityName).append("CustomizedMapper").append("]已经存在，不重复生成").toString());
                    continue;
                }

                String mapperXmlContent = generatorMapperXmlContent.get(entityName);
                String baseColumnList = StringUtils.substring(mapperXmlContent, StringUtils.indexOf(mapperXmlContent, "<sql id=\"Base_Column_List\">"), StringUtils.indexOf(mapperXmlContent, "<select id=\"selectByCriteria\""));

                baseColumnList = StringUtils.replaceAll(baseColumnList, "<sql id=\"Base_Column_List\">", "");
                baseColumnList = StringUtils.replaceAll(baseColumnList, "</sql>", "");

                String[] columnArr = StringUtils.split(baseColumnList, ",");
                baseColumnList = "";

                int length = columnArr.length;
                for (int i = 0; i < length; i++) {
                    String column = columnArr[i];
                    if (i != length - 1) {
                        baseColumnList += "t1." + column.trim() + ", ";
                    } else {
                        baseColumnList += "t1." + column.trim();
                    }
                }

                Date date = new Date();

                String mapperContent = content.replace("${customizedMapperPackage}", this.customizedMapperPackage).replace("${dtoPackage}", this.dtoPackage).replace("${mapperPackage}", this.mapperPackage);

                mapperContent = mapperContent.replaceAll("\\$\\{entityName\\}", entityName);
                mapperContent = mapperContent.replaceAll("\\$\\{tableName\\}", tableNames.get(entityName));
                mapperContent = mapperContent.replaceAll("\\$\\{Base_Column_List\\}", baseColumnList);
                mapperContent = mapperContent.replaceAll("\\$\\{date\\}", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));

                FileWriter writer = new FileWriter(target);
                IOUtils.write(mapperContent, writer);

                if (writer != null) {
                    writer.close();
                }

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.customizedMapperPackage).append('.').append(entityName).append("CustomizedMapper").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info("genCustomizedMapperXml", e);
        }
    }

    private void genDubboServiceXml(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            InputStream dubboService = new FileInputStream(templateDir + "/template/dubboService.ftl");
            String dubboServiceFtlContent = IOUtils.toString(dubboService, "UTF-8");

            if (dubboService != null) {
                dubboService.close();
            }

            String dubboConfigPath = new StringBuilder().append(this.webappBaseDir).append(this.dubboConfigPackage).toString();
            String dubboServiceFilePath = new StringBuilder().append(dubboConfigPath).append("ssbService.xml").toString();
            for (String entityName : entityNames) {
                if (excludeGenServiceEntityList.contains(entityName)) {
                    /**
                     * 不生成service的entity跳过
                     */
                    getLog().info(entityName + "不生成" + "[" + servicePackage + "." + entityName + "ServiceImpl]");

                    continue;
                }

                File ssbService = new File(dubboServiceFilePath);
                InputStream ssbServiceInputStream = new FileInputStream(ssbService);
                String oldServiceContent = IOUtils.toString(ssbServiceInputStream, "UTF-8");

                if (ssbServiceInputStream != null) {
                    ssbServiceInputStream.close();
                }

                /**
                 * 生成dubbo-service
                 */
                String serviceName = FileUtil.changeCharToLower(entityName, 1);
                if (oldServiceContent.contains("id=\"" + serviceName + "Service\"")) {
                    getLog().warn("[" + servicePackage + "." + entityName + "ServiceImpl]已配置过DubboService");
                    continue;
                }

                Date date = new Date();

                String newServiceContent = oldServiceContent.substring(0, oldServiceContent.indexOf("</beans>"));
                newServiceContent += dubboServiceFtlContent.replaceAll("\\$\\{servicePackage\\}", this.servicePackage).replaceAll("\\$\\{date\\}", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date})).replaceAll("\\$\\{entryName\\}", entityName).replaceAll("\\$\\{schema\\}", getSchemaByDomainObjectName(entityName)).replaceAll("\\$\\{idName\\}", serviceName);
                newServiceContent += "</beans>";

                FileWriter writer = new FileWriter(dubboServiceFilePath);
                IOUtils.write(newServiceContent, writer);

                if (writer != null) {
                    writer.close();
                }

                getLog().info("添加[" + servicePackage + "." + entityName + "ServiceImpl]到DubboService配置中");
            }
        } catch (Exception e) {
            getLog().info("genDubboServiceXml", e);
        }
    }

    private void genDubboProviderXml(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());
            InputStream dubboProvider = new FileInputStream(templateDir + "/template/dubboProvider.ftl");
            String dubboProviderFtlContent = IOUtils.toString(dubboProvider, "UTF-8");

            if (dubboProvider != null) {
                dubboProvider.close();
            }

            String dubboConfigPath = new StringBuilder().append(this.webappBaseDir).append(this.dubboConfigPackage).toString();
            String dubboProviderFilePath = new StringBuilder().append(dubboConfigPath).append("ssbProvider.xml").toString();
            for (String entityName : entityNames) {
                if (excludeGenServiceEntityList.contains(entityName)) {
                    /**
                     * 不生成service的entity跳过
                     */
                    getLog().info(entityName + "不生成" + "[" + servicePackage + ".I" + entityName + "Service]");

                    continue;
                }

                File ssbProvider = new File(dubboProviderFilePath);
                InputStream ssbProviderInputStream = new FileInputStream(ssbProvider);
                String oldProviderContent = IOUtils.toString(ssbProviderInputStream, "UTF-8");

                if (ssbProviderInputStream != null) {
                    ssbProviderInputStream.close();
                }

                if (oldProviderContent.contains("interface=\"" + servicePackage + ".I" + entityName + "Service\"")) {
                    getLog().warn("[" + servicePackage + ".I" + entityName + "Service]已配置过DubboProvider");
                    continue;
                }

                /**
                 * 生成dubbo-provider
                 */

                Date date = new Date();

                String newProvideContent = oldProviderContent.substring(0, oldProviderContent.indexOf("</beans>"));
                newProvideContent += dubboProviderFtlContent.replaceAll("\\$\\{servicePackage\\}", this.servicePackage).replaceAll("\\$\\{date\\}", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date})).replaceAll("\\$\\{entryName\\}", entityName).replaceAll("\\$\\{schema\\}", getSchemaByDomainObjectName(entityName)).replaceAll("\\$\\{idName\\}", FileUtil.changeCharToLower(entityName, 1));
                newProvideContent += "</beans>";

                FileWriter writer = new FileWriter(dubboProviderFilePath);
                IOUtils.write(newProvideContent, writer);

                if (writer != null) {
                    writer.close();
                }

                getLog().info("添加[" + servicePackage + ".I" + entityName + "Service]到DubboProvider配置中");
            }
        } catch (Exception e) {
            getLog().info("genDubboProviderXml", e);
        }
    }

    private void genDubboConsumerXml(String entityName) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());
            InputStream dubboConsumer = new FileInputStream(templateDir + "/template/dubboConsumer.ftl");
            String dubboConsumerFtlContent = IOUtils.toString(dubboConsumer, "UTF-8");

            if (dubboConsumer != null) {
                dubboConsumer.close();
            }

            String dubboConfigPath = new StringBuilder().append(this.dubboConsumerPath).append(this.dubboConfigPackage).toString();
            String dubboServiceFilePath = new StringBuilder().append(dubboConfigPath).append("ssbConsumer.xml").toString();
            File ssbConsumer = new File(dubboServiceFilePath);

            if (ssbConsumer == null || !ssbConsumer.exists()) {
                getLog().error("系统找不到ssbConsumer.xml文件, 请检查POM工程文件目录配置");
            }

            InputStream ssbConsumerInputStream = new FileInputStream(ssbConsumer);
            String oldServiceContent = IOUtils.toString(ssbConsumerInputStream, "UTF-8");

            if (ssbConsumerInputStream != null) {
                ssbConsumerInputStream.close();
            }

            /**
             * 生成dubbo-service
             */
            String serviceName = FileUtil.changeCharToLower(entityName, 1);
            if (oldServiceContent.contains("id=\"" + serviceName + "Service\"")) {
                getLog().warn("应用对象[" + servicePackage + ".I" + entityName + "Service]已配置过DubboConsumer");
                return;
            }

            Date date = new Date();

            String newServiceContent = oldServiceContent.substring(0, oldServiceContent.indexOf("</beans>"));
            newServiceContent += dubboConsumerFtlContent.replaceAll("\\$\\{servicePackage\\}", this.servicePackage).replaceAll("\\$\\{entryName\\}", entityName).replaceAll("\\$\\{date\\}", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date})).replaceAll("\\$\\{schema\\}", getSchemaByDomainObjectName(entityName)).replaceAll("\\$\\{idName\\}", serviceName);
            newServiceContent += LINE_SEPARATOR;
            newServiceContent += "</beans>";

            FileWriter writer = new FileWriter(dubboServiceFilePath);
            IOUtils.write(newServiceContent, writer);

            if (writer != null) {
                writer.close();
            }

            getLog().info("添加[" + servicePackage + ".I" + entityName + "Service]到DubboConsumer配置中");

        } catch (Exception e) {
            getLog().info("genDubboConsumerXml", e);
        }
    }

    private void genController(Set<String> entityNames) {
        try {
            String userHome = System.getProperty("user.home");
            URL resource = GenMojo.class.getResource("");
            String jarPath = resource.getPath().substring(0, resource.getPath().indexOf("!"));

            File templateDir = new File(new StringBuilder().append(userHome).append(File.separator).append("mybatis开发工具包").append(File.separator).append("gen").append(File.separator).toString());
            if (!templateDir.exists()) {
                templateDir.mkdirs();
                getLog().debug(new StringBuilder().append("新建开发工具包模版目录[path=").append(templateDir.getPath()).append("]").toString());
            }

            decompress(URLDecoder.decode(jarPath.substring(5), "UTF-8"), new StringBuilder().append(templateDir.getPath()).append(File.separator).toString());

            TEMPLATE_CFG.setDefaultEncoding("UTF-8");
            TEMPLATE_CFG.setDirectoryForTemplateLoading(new File(new StringBuilder().append(templateDir.getPath()).append(File.separator).append("template").toString()));

            Template template = TEMPLATE_CFG.getTemplate("controller.ftl");
            template.setOutputEncoding("UTF-8");

            String controllerPath = new StringBuilder().append(this.controllerTargetPath).append(this.controllerPackage.replace(".", "/")).toString();
            File controllerFile = new File(controllerPath);
            if (!controllerFile.exists()) {
                controllerFile.mkdirs();
            }

            for (String entityName : entityNames) {

                if (!genControllerEntityList.contains(entityName)) {
                    /**
                     * 不生成controller的entity跳过
                     */
                    getLog().info(entityName + "不生成controller");
                    continue;
                }

                /**
                 * 生成DubboConsumer信息
                 */
                genDubboConsumerXml(entityName);

                File target = new File(new StringBuilder().append(controllerPath).append(File.separator).append(entityName).append("Controller.java").toString());
                if (target.exists()) {
                    getLog().warn(new StringBuilder().append("应用对象[").append(this.controllerPackage).append('.').append(entityName).append("Controller").append("]已经存在，不重复生成").toString());
                    continue;
                }

                Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(target), "UTF-8"));
                Map dataModel = new HashMap(16);
                Date date = new Date();
                dataModel.put("controllerPackage", this.controllerPackage);
                dataModel.put("dtoPackage", this.dtoPackage);
                dataModel.put("searchDTOPackage", this.searchDTOPackage);
                dataModel.put("servicePackage", this.servicePackage);
                dataModel.put("entityPackage", this.entityPackage);
                dataModel.put("dateTime", this.dateFormat.format(date));
                dataModel.put("date", String.format(Locale.CHINA, "%1$tb %2$td, %3$tY", new Object[]{date, date, date}));
                dataModel.put("entityName", entityName);
                String humpEntityName = StringUtils.lowerCase(entityName.substring(0, 1)) + entityName.substring(1);
                dataModel.put("humpEntityName", humpEntityName);

                dataModel.put("author", author);
                dataModel.put("copyright", copyright);

                String schema = getSchemaByDomainObjectName(entityName);
                dataModel.put("schema", schema);

                template.process(dataModel, out);

                out.flush();
                out.close();

                getLog().info(new StringBuilder().append("生成应用对象[").append(this.controllerPackage).append('.').append(entityName).append("Controller").append("]").toString());
            }
        } catch (Exception e) {
            getLog().info("genController", e);
        }
    }

    private static void decompress(String fileName, String outputPath) throws IOException {
        JarFile jarFile = new JarFile(fileName);
        Enumeration e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry je = (JarEntry) e.nextElement();

            if (!je.getName().startsWith("template")) {
                continue;
            }
            String outFileName = new StringBuilder().append(outputPath).append(je.getName()).toString();

            File f = new File(outFileName);

            if (je.isDirectory()) {
                f.mkdir();
                continue;
            }

            InputStream in = jarFile.getInputStream(je);
            OutputStream out = new BufferedOutputStream(new FileOutputStream(f));

            byte[] buffer = new byte[2048];
            int nBytes;

            while ((nBytes = in.read(buffer)) > 0) {
                out.write(buffer, 0, nBytes);
            }

            out.flush();
            out.close();
            in.close();
        }
    }

    private void runScriptIfNecessary() throws MojoExecutionException {
        if (this.sqlScript == null) {
            return;
        }

        SqlScriptRunner scriptRunner = new SqlScriptRunner(this.sqlScript, this.jdbcDriver, this.jdbcURL, this.jdbcUserId, this.jdbcPassword);
        scriptRunner.setLog(getLog());
        scriptRunner.executeScript();
    }

    private void calculateClassPath() throws MojoExecutionException {
        if (includeCompileDependencies || includeAllDependencies) {
            try {

                List<String> entries = new ArrayList<>();
                if (includeCompileDependencies) {
                    entries.addAll(project.getCompileClasspathElements());
                }

                if (includeAllDependencies) {
                    entries.addAll(project.getTestClasspathElements());
                }

                entries.remove(project.getBuild().getOutputDirectory());
                entries.remove(project.getBuild().getTestOutputDirectory());

                ClassLoader contextClassLoader = ClassloaderUtility.getCustomClassloader(entries);
                Thread.currentThread().setContextClassLoader(contextClassLoader);

            } catch (DependencyResolutionRequiredException e) {
                throw new MojoExecutionException("Dependency Resolution Required", e);
            }
        }
    }

    private void saveClassLoader() {
        savedClassloader.set(Thread.currentThread().getContextClassLoader());
    }

    @Override
    public File getOutputDirectory() {
        return this.outputDirectory;
    }

    static {
        TEMPLATE_CFG.setOutputEncoding("UTF-8");
    }
}