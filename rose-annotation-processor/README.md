# Rose Annotation Processor

基于 JSR 269 规范的注解处理器，用于在编译期自动生成配置属性元数据和服务加载器配置。

## 项目概览

Rose Annotation Processor 提供两个核心注解处理器：

- **ConfigurationPropertyAnnotationProcessor** - 配置属性元数据生成器
- **AutoServiceProcessor** - Java SPI 服务自动注册器

**技术栈：**
- Java 8+
- JSR 269 Pluggable Annotation Processing API
- Maven 构建

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>io.github.rosestack</groupId>
    <artifactId>rose-annotation-processor</artifactId>
    <version>${rose.version}</version>
    <scope>provided</scope>
</dependency>
```

### 2. 使用 @ConfigurationProperty

```java
public class DatabaseConfig {
    @ConfigurationProperty(
        name = "db.url",
        type = String.class,
        defaultValue = "jdbc:mysql://localhost:3306/mydb",
        required = true,
        description = "数据库连接URL"
    )
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
}
```

编译后自动生成 `META-INF/rose/configuration-properties.json`。

### 3. 使用 @AutoService

```java
@AutoService(DataSource.class)
public class HikariDataSourceImpl implements DataSource {
    // 实现代码
}
```

编译后自动生成 `META-INF/services/javax.sql.DataSource`。

## 功能特性

### 1. ConfigurationProperty 注解处理器

自动扫描 `@ConfigurationProperty` 注解，生成配置属性元数据文件 `META-INF/rose/configuration-properties.json`。

**使用示例：**

```java
public class AppConfig {

    @ConfigurationProperty(
        name = "app.name",
        type = String.class,
        defaultValue = "MyApp",
        required = true,
        description = "应用名称",
        source = "application.properties"
    )
    private static final String APP_NAME = "MyApp";
}
```

**生成的 JSON 格式：**

```json
[
  {
    "name": "app.name",
    "type": "java.lang.String",
    "defaultValue": "MyApp",
    "required": true,
    "description": "应用名称",
    "metadata": {
      "sources": ["application.properties"],
      "declaredClass": "com.example.AppConfig",
      "declaredField": "APP_NAME"
    }
  }
]
```

### 2. AutoService 注解处理器

自动生成 `META-INF/services` 文件，简化 Java SPI 服务注册。

**使用示例：**

```java
@AutoService(MyService.class)
public class MyServiceImpl implements MyService {
    // 实现代码
}
```

**自动生成：** `META-INF/services/com.example.MyService`

## JSR 269 API 核心概念

### 1. 注解处理器生命周期

```
编译开始 → init() → getSupportedAnnotationTypes()
         → process() → [处理注解] → 编译结束
```

### 2. 核心接口

| 接口 | 说明 |
|------|------|
| `Processor` | 注解处理器基础接口 |
| `AbstractProcessor` | 抽象实现类，推荐继承 |
| `ProcessingEnvironment` | 处理环境，提供工具类访问 |
| `RoundEnvironment` | 当前处理轮次的环境信息 |

### 3. 关键工具类

```java
// 获取工具类
Elements elementUtils = processingEnv.getElementUtils();
Types typeUtils = processingEnv.getTypeUtils();
Filer filer = processingEnv.getFiler();
Messager messager = processingEnv.getMessager();

// 元素操作
Element element = ...;
String qualifiedName = elementUtils.getQualifiedName(element);
TypeMirror typeMirror = element.asType();

// 生成文件
FileObject resource = filer.createResource(
    StandardLocation.CLASS_OUTPUT,
    "",
    "META-INF/rose/config.json"
);
```

### 4. 注解处理器注册

**方式一：SPI 配置**

创建文件 `META-INF/services/javax.annotation.processing.Processor`：

```
com.example.MyAnnotationProcessor
```

**方式二：使用 @AutoService**

```java
@AutoService(Processor.class)
public class MyAnnotationProcessor extends AbstractProcessor {
    // ...
}
```

### 5. 常见注解

```java
@SupportedAnnotationTypes("com.example.MyAnnotation")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedOptions({"debug", "verbose"})
public class MyProcessor extends AbstractProcessor {
    // ...
}
```

## 开发指南

### 构建项目

```bash
# 编译
mvn clean compile

# 运行测试
mvn test

# 生成覆盖率报告
mvn test jacoco:report

# 代码格式化
mvn spotless:apply
```

### 测试注解处理器

本项目使用 **Java Compiler API** 进行注解处理器的集成测试。详细说明请参考 [TESTING.md](TESTING.md)。

**核心测试方法：**

```java
@Test
void testProcessor(@TempDir Path tempDir) throws IOException {
    // 1. 创建测试源文件
    // 2. 使用 JavaCompiler 编译
    // 3. 验证生成的元数据文件
}
```

### 禁用注解处理

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <compilerArgument>-proc:none</compilerArgument>
    </configuration>
</plugin>
```

## 参考资源

- [JSR 269 规范](https://jcp.org/en/jsr/detail?id=269)
- [Annotation Processing 101](https://hannesdorfmann.com/annotation-processing/annotationprocessing101/)
- [Google Auto Service](https://github.com/google/auto/tree/master/service)
- [Rose Stack 项目主页](https://github.com/chensoul/rose-playground)


