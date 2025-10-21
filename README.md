# Rose Java

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/rosestack/rose-java)
[![Maven Build](https://github.com/rosestack/rose-java/actions/workflows/maven-build.yml/badge.svg)](https://github.com/rosestack/rose-java/actions/workflows/maven-build.yml)
[![Maven](https://img.shields.io/maven-central/v/io.github.rosestack/rose-java.svg)](https://central.sonatype.com/artifact/io.github.rosestack/rose-java)
[![Codecov](https://codecov.io/gh/rosestack/rose-java/branch/main/graph/badge.svg)](https://app.codecov.io/gh/rosestack/rose-java)
![License](https://img.shields.io/github/license/rosestack/rose-java.svg)
[![Average time to resolve an issue](https://isitmaintained.com/badge/resolution/rosestack/rose-java.svg)](https://isitmaintained.com/project/rosestack/rose-java "Average time to resolve an issue")
[![Percentage of issues still open](https://isitmaintained.com/badge/open/rosestack/rose-java.svg)](https://isitmaintained.com/project/rosestack/rose-java "Percentage of issues still open")

Rose Java 核心库，提供通用工具类和注解处理器。

## 模块

### rose-java-core
核心工具类库，提供常用的工具方法。

**Maven 依赖：**
```xml
<dependency>
    <groupId>io.github.rosestack</groupId>
    <artifactId>rose-java-core</artifactId>
    <version>0.0.1</version>
</dependency>
```

### rose-annotation-processor
注解处理器，支持编译期代码生成。

**Maven 依赖：**
```xml
<dependency>
    <groupId>io.github.rosestack</groupId>
    <artifactId>rose-annotation-processor</artifactId>
    <version>0.0.1</version>
    <scope>provided</scope>
</dependency>
```

**功能特性：**
- `@ConfigurationProperty` - 自动生成配置属性元数据
- `@AutoService` - 自动生成 ServiceLoader 配置文件

详见：[rose-annotation-processor/README.md](rose-annotation-processor/README.md)

## 快速开始

### 1. 添加依赖

在项目 `pom.xml` 中添加 BOM 依赖管理：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.github.rosestack</groupId>
            <artifactId>rose-java-bom</artifactId>
            <version>0.0.1-SNAPSHOT</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

然后添加所需模块（无需指定版本）：

```xml
<dependencies>
    <!-- 核心工具类 -->
    <dependency>
        <groupId>io.github.rosestack</groupId>
        <artifactId>rose-java-core</artifactId>
    </dependency>

    <!-- 注解处理器 -->
    <dependency>
        <groupId>io.github.rosestack</groupId>
        <artifactId>rose-annotation-processor</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### 2. 使用工具类

```java


// 日期处理
String dateStr = DateUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");

        // 敏感信息脱敏
        String maskedPhone = SensitiveUtils.desensitize("13800138000", SensitiveType.MOBILE_PHONE);
// 输出: 138****8000

        // 生成唯一 ID
        String nanoId = NanoId.randomNanoId();
        String uuid = Uuids.timeBased().toString();

        // JSON 处理
        String json = JsonUtils.toJson(object);
        MyObject obj = JsonUtils.fromJson(json, MyObject.class);
```

### 3. 使用注解处理器

```java
// 配置属性元数据
@ConfigurationProperty(
    name = "server.port",
    type = Integer.class,
    defaultValue = "8080",
    required = true,
    description = "服务器端口"
)
private static final int SERVER_PORT = 8080;

// ServiceLoader 自动注册
@AutoService(MyService.class)
public class MyServiceImpl implements MyService {
    // 实现代码
}
```

## 构建项目

```bash
# 编译
mvn clean compile

# 运行测试
mvn test

# 查看测试覆盖率
mvn test jacoco:report

# 安装到本地仓库
mvn clean install

# 跳过测试安装
mvn clean install -DskipTests

# 代码格式化
mvn spotless:apply
```

## 环境要求

- **Java**: 1.8+
- **Maven**: 3.9+

## 许可证

[Apache License 2.0](LICENSE)
