package com.baomidou.plugin.idea.mybatisx.codegenerator.domain;

/**
 * 代码生成配置
 */
public class GenConfig {

    private String rootFolder;

    private Long id;

    /** 包路径 **/
    private String pack = "org.py.mybmodule.submodule";

    /** 模块名 **/
    private String moduleName = "mybmodule";

    /** 前端文件路径 **/
    private String path;

    /** 前端文件路径 **/
    private String apiPath;

    /** 作者 **/
//    private String author;

    /** 表前缀 **/
    private String prefix;

    /** 是否覆盖 **/
    private boolean cover = false;

    private String dbUrl;
    private String jdbcDriver;
    private String username;
    private String password;

//    private String module = "mybmodule";
    private String author = "author";

    private boolean isLombok = true;
    private boolean isSwagger = true;
    private boolean isRestcontroller = true;
    private boolean isResultmap = false;
    private boolean isFill = false;

    private String templatePath = "/templates";
    private String entityName = "entity";
    private String mapperName = "mapper";
    private String controllerName = "controller";
    private String serviceName = "service";
    private String serviceImplName = "service.impl";

    private int idtype = 0;

    private boolean isEnableCache = false;
    private boolean isBaseColumnList = false;

    public boolean isBaseColumnList() {
        return isBaseColumnList;
    }

    public void setBaseColumnList(boolean baseColumnList) {
        isBaseColumnList = baseColumnList;
    }

    public boolean isEnableCache() {
        return isEnableCache;
    }

    public void setEnableCache(boolean enableCache) {
        isEnableCache = enableCache;
    }

    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public void setDbUrl(String dbUrl) {
        this.dbUrl = dbUrl;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public void setJdbcDriver(String jdbcDriver) {
        this.jdbcDriver = jdbcDriver;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isCover() {
        return cover;
    }

    public void setCover(boolean cover) {
        this.cover = cover;
    }

    public boolean isLombok() {
        return isLombok;
    }

    public void setLombok(boolean lombok) {
        isLombok = lombok;
    }

    public boolean isSwagger() {
        return isSwagger;
    }

    public void setSwagger(boolean swagger) {
        isSwagger = swagger;
    }

    public boolean isRestcontroller() {
        return isRestcontroller;
    }

    public void setRestcontroller(boolean restcontroller) {
        isRestcontroller = restcontroller;
    }

    public boolean isResultmap() {
        return isResultmap;
    }

    public void setResultmap(boolean resultmap) {
        isResultmap = resultmap;
    }

    public boolean isFill() {
        return isFill;
    }

    public void setFill(boolean fill) {
        isFill = fill;
    }

    public String getEntityName() {
        return entityName;
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getControllerName() {
        return controllerName;
    }

    public void setControllerName(String controllerName) {
        this.controllerName = controllerName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceImplName() {
        return serviceImplName;
    }

    public void setServiceImplName(String serviceImplName) {
        this.serviceImplName = serviceImplName;
    }

    public int getIdtype() {
        return idtype;
    }

    public void setIdtype(int idtype) {
        this.idtype = idtype;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPack() {
        return pack;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getApiPath() {
        return apiPath;
    }

    public void setApiPath(String apiPath) {
        this.apiPath = apiPath;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }


    public String getRootFolder() {
        return rootFolder;
    }

    public void setRootFolder(String rootFolder) {
        this.rootFolder = rootFolder;
    }
}
