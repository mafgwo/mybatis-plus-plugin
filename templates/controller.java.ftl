package ${package.Controller};

import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import ${package.Service}.${table.serviceName};
import ${package.Entity}.${table.entityName};
import ${cfg.obj}.Result;
<#if swagger2>
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Api;
</#if>

<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>

/**
 * <p>
 * ${table.comment!} 控制器
 * </p>
 *
 * @author ${author}
 * @since ${date}
 */
<#if swagger2>
@Api(tags = "${entity}管理")
</#if>
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if camelTableNameStyle??>${camelTableName}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
  <#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
  <#else>
public class ${table.controllerName} {
  </#if>

    @Autowired
    private ${table.serviceName} ${cfg.camelTableName}Service;

<#if swagger2>    @ApiOperation(value = "查询${entity}")</#if>
    @GetMapping(value = "/page")
    public Result<IPage> get${entity}Page(Page<${entity}> page, ${entity} ${cfg.camelTableName}){
        return new Result<>(${cfg.camelTableName}Service.page(page,Wrappers.query(${cfg.camelTableName})) );
    }

<#if swagger2>    @ApiOperation(value = "新增${entity}")</#if>
    @PostMapping(value = "/add")
    public Result create(@Valid @RequestBody ${entity} ${cfg.camelTableName}, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return new Result<>(fieldErrors);
        }
        return new Result<>(${cfg.camelTableName}Service.save(${cfg.camelTableName}));
    }

<#if swagger2>    @ApiOperation(value = "修改${entity}")</#if>
    @PutMapping(value = "/update")
    public Result update(@Valid @RequestBody ${entity} ${cfg.camelTableName}, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return new Result<>(fieldErrors);
         }
        return new Result<>(${cfg.camelTableName}Service.updateById(${cfg.camelTableName}));
    }

    <#list table.fields as field>
         <#if field.keyFlag>
    <#if swagger2>@ApiOperation(value = "删除${entity}")</#if>
    @DeleteMapping(value = "/delete/{${field.propertyName}}")
    public Result delete(@PathVariable ${field.propertyType} ${field.propertyName}){
        return new Result<>(${cfg.camelTableName}Service.removeById(${field.propertyName}));
    }
         </#if>
    </#list>
}
</#if>
