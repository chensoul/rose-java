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
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

### rose-annotation-processor
注解处理器，支持编译期代码生成。

**Maven 依赖：**
```xml
<dependency>
    <groupId>io.github.rosestack</groupId>
    <artifactId>rose-annotation-processor</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <scope>provided</scope>
</dependency>
```

**功能特性：**
- `@ConfigurationProperty` - 自动生成配置属性元数据
- `@AutoService` - 自动生成 ServiceLoader 配置文件

详见：[rose-annotation-processor/README.md](rose-annotation-processor/README.md)

## 构建

```bash
# 编译
mvn clean compile

# 测试
mvn test

# 安装到本地仓库
mvn clean install

# 跳过测试安装
mvn clean install -DskipTests
```

## 环境要求

- **Java**: 1.8+
- **Maven**: 3.9+

## 许可证

Apache License 2.0
