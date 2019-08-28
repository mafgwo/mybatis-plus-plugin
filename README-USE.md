## 使用代码生成

1 idea -> Other

### 配置mysql信息

2 config database

### 配置代码生成路径 生成代码

3 code generator

#### 配置说明

* module 子项目名称

如果有多个子项目可以设置module得值

平台 | module值
---|---
windows| module\\submodule\\submodule
linux | module/submodule/submodule
  
* package 包路径

###  注意

把templates文件夹复制到项目根目录下，可以自定义修改模板，
这里要注意模板里面的变量，如果没有变量会报错。

### 自定义controller生成CURD

1 找自定义文件，
[自定义文件路径](https://github.com/kana112233/mybatis-plus-plugin/templates/)

2 复制到项目根目录下的templates文件夹下

3 执行最上面的 `使用代码生成123` 操作

          




