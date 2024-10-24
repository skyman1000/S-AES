# 用户指南

## 目录

1. [简介](#简介)
2. [系统要求](#系统要求)
3. [安装与启动](#安装与启动)
4. [界面介绍](#界面介绍)
5. [功能使用](#功能使用)
    - [1. 基本加密与解密](#1-基本加密与解密)
    - [2. ASCII字符串加密与解密](#2-asciistring加密与解密)
    - [3. 多重加密与解密](#3-多重加密与解密)
    - [4. 中间相遇攻击破解密钥](#4-暴力破解密钥)
    - [5. CBC模式加密与解密](#5-cbc模式加密与解密)
6. [常见问题与解决方法](#常见问题与解决方法)

    

---

## 简介

欢迎使用S-AES加解密工具！本工具基于简化的数据加密标准（S-AES）算法，提供了加密、解密、中间相遇攻击破解密钥、CBC加解密等多种功能。通过直观的图形用户界面（GUI），您可以轻松地对二进制数据、ASCII字符串进行加密与解密操作。此外，工具还支持多重加密和密码分组链接（CBC）模式，满足不同安全需求。

## 系统要求

- **操作系统**：Windows、macOS 或 Linux
- **Java版本**：Java SE 8或更高版本

## 安装与启动

1. **下载项目文件**：
   
- 获取项目压缩包或通过版本控制系统（如Git）克隆源码库。
  
2. **解压与配置**：
   
- 将下载的项目文件解压到您选择的目录中。
  
3. **编译项目**：
    - 使用集成开发环境（IDE）IntelliJ IDEA，或通过命令行编译`SDESGUI.java`文件。
    - 确保项目结构如下：
      ```plaintext
      ├── picture                         - 存放结果数据和图片
      ├── src
      │    ├── AES
      │    │    ├── S_AES.java            - S-DES算法实现
      │    │    └── SAESGUI.java          - 界面设计   
      └── README.md                       - 用户指南
      ```

4. **运行GUI界面**：
   
    - 在IDE中运行`SAESGUI.java`的`main`方法
    - 界面启动后，您将看到S-AES加解密工具的主窗口。

## 界面介绍

(界面截图)

主界面主要分为以下几个部分：（根据具体情况修改）

1. **输入区域**：
    - **明文输入框**：输入待加密的二进制数据或字符串。
    - **密钥输入框**：输入二进制密钥。
    - **密文显示框**：显示加密后的密文或解密后的明文。

2. **操作按钮**：
    - **加密**：执行加密操作。
    - **解密**：执行解密操作。
    - **中间相遇攻击破解**：尝试通过已知明文和密文破解密钥。
    - **多重加密/解密**：根据密钥长度执行多重加密或解密。
    - **CBC模式加密/解密**：在CBC模式下进行加密与解密。

    3.选择加解密模式：
    
    （根据具体情况说明）
    
3. **日志区域**：
   
    - 显示操作日志，包括成功信息、错误提示及暴力破解结果。

## 功能使用

### 1. 基本加密与解密

**用途**：对16位二进制明文进行加密，或对16位二进制密文进行解密。

**步骤**：

1. **输入明文**：
    - 在“明文（16-bit 或字符串）”输入框中输入16位二进制字符串（如`1010101010101010`），或输入ASCII字符（如`A`）。

2. **输入密钥**：
    - 在“密钥（16-bit）”输入框中输入16位二进制密钥（如`1010101010101010`）

3. **执行加密**：
    - 点击“加密”按钮，密文将显示在“密文”框中，日志区域显示“加密成功！”。

4. **执行解密**：
    - 确保“密文”框中有有效密文，输入相同的密钥。
    - 点击“解密”按钮，解密后的明文将显示在“密文”框中，日志区域显示“解密成功！”。

**示例**：

- **明文**：
- **密钥**：``
- **加密结果**：``
- **解密结果**：``

### 2. ASCII字符串加密与解密

**用途**：对ASCII字符串进行逐字符加密与解密。

**步骤**：

1. **输入明文**：
    - 在“明文”输入框中输入任意ASCII字符串（如`Hello`）。

2. **输入密钥**：
    - 输入16位二进制密钥（如`1010001110100011`），支持双重加密。

3. **执行加密**：
    - 点击“加密”按钮，加密后的密文列表将显示在“密文”框中，每个字符对应一个16位密文。

4. **执行解密**：
    - 确保“密文”框中有有效密文，输入相同的密钥。
    - 点击“解密”按钮，解密后的ASCII字符串将显示在“密文”框中。

**示例**：

- **明文**：`Hello`
- **密钥**：`1010001110100011`
- **加密结果**：[`0011111000110010`, `0010100101000101`, `1000011100011101`, `0100001100110110`, `1000000111111110`]
- **解密结果**：`Hello`

### 3. 多重加密与解密

**用途**：通过使用多个子密钥对明文进行多轮加密，提高加密强度。

**步骤**：

1. **输入明文**：
    - 输入16位二进制明文（如`1001100101100101`），或对应的ASCII/Unicode字符串。

2. **输入密钥**：
    - 输入32位或48位二进制密钥，支持双重（32位）或三重加密（48位）。

3. **执行多重加密**：
    - 点击“加密”按钮，多重加密后的密文将显示在“密文”框中。

4. **执行多重解密**：
    - 输入相同的多重密钥。
    - 点击“解密”按钮，解密后的明文将显示在“密文”框中。

**示例**：

- **明文**：
- **密钥**：
- **加密结果**：``
- **解密结果**：

### 4. 中间相遇攻击破解密钥

**用途**：通过已知的明文和密文对，尝试找到对应的密钥。

**步骤**：

1. **输入已知明文**：
    - 输入16位二进制明文（如）。

2. **输入已知密文**：
    - 输入对应的16位二进制密文（如）。

3. **执行中间相遇攻击**：
    - 点击“执行中间相遇攻击”按钮，程序将开始进行密钥破解。
    - 破解结果将在日志区域显示，列出所有符合条件的密钥。

**示例**：

- **明文**：
- **密文**：
- **破解结果**：

**注意**：执行中间相遇攻击可能会耗费一定时间，具体取决于计算机性能。

### 5. CBC模式加密与解密

**用途**：在密码分组链接（CBC）模式下对较长的明文进行加密与解密，增强加密安全性。

**步骤**：

1. **输入明文**：
    - 输入长度为16的倍数的二进制字符串（如）。

2. **输入密钥**：
    - 输入16位二进制密钥。

3. **输入初始向量（IV）**：
    - 输入16位二进制初始向量（IV），确保加密和解密双方共享相同的IV。

4. **执行CBC加密**：
    - 点击“加密”按钮，加密后的密文列表将显示在“密文”框中。

5. **执行CBC解密**：
    - 输入相同的密钥和IV。
    - 点击“解密”按钮，解密后的明文将显示在“密文”框中。

6. **篡改密文测试**：
    - 在密文列表中选择一个密文块，修改其内容（如全部置为）。
    - 点击“解密”按钮，观察解密结果的变化，验证CBC模式对密文完整性的敏感性。

**示例**：

- **明文**：
- **密钥**：
- **初始向量（IV）**：
- **加密结果**：[``]
- **解密结果**：
- **篡改密文**：将第二个密文块修改为
- **篡改后的解密结果**：

## 常见问题与解决方法

### 1. 密钥和明文的位数不匹配怎么办？

确保密钥和明文的位数符合要求：

- **基本加密与解密**：
    - 明文：16位二进制字符串或ASCII字符。
    - 密钥：16位二进制字符串。

- **多重加密**：
    - 密钥长度应为16位的整数倍（如16位表示双重加密，32位表示三重加密）。
    - 明文长度应为16位的整数倍。

- **CBC模式**：
    - 明文长度必须为16位的倍数，不足部分需右侧补零。
    - 密钥长度为16位。

### 2. 为什么解密后的明文与原文不一致？

可能的原因包括：

- **密钥输入错误**：确保加密和解密时使用相同的密钥。
- **密文被篡改**：在CBC模式下，密文的任何修改都会影响解密结果。
- **初始向量（IV）不同**：加密和解密时必须使用相同的IV。
- **明文或密文长度不正确**：确保明文和密文长度符合算法要求，必要时进行补零。

## 附录

### A. 密钥与明文长度对照表

| 功能类型    | 明文长度         | 密钥长度                     |
| ----------- | ---------------- | ---------------------------- |
| 基本加密    | 16位二进制或字符 | 16位二进制                   |
| 多重加密    | 16位二进制或字符 | 16位的整数倍（如32位、48位） |
| CBC模式加密 | 16位的倍数       | 16位二进制                   |
| ASCII加密   | 任意长度字符串   | 16位二进制                   |

## 注意事项

1. **密钥长度**：
    - **基本加密与解密**：密钥必须为10位或16位二进制字符串。
    - **多重加密**：密钥长度应为10位的整数倍，如20位（双重加密）、30位（三重加密）等。

2. **明文与密文长度**：
    - **基本加密与解密**：支持8位二进制字符串或单个ASCII字符。
    - **多重加密**：支持8位二进制字符串，密钥长度决定加密轮数。
    - **CBC模式**：明文长度必须为10位的倍数，不足部分需右侧补零。

3. **输入格式**：
    - 明文和密文输入框支持二进制字符串和ASCII/Unicode字符串。
    - 密钥输入框仅支持二进制字符串。

4. **错误处理**：
    - 加密与解密过程中，若输入格式不正确或长度不匹配，系统会弹出错误提示。
    - 建议用户在操作前确保输入数据的正确性。

5. **安全性**：
    - S-DES为简化加密标准，适合教学与学习用途，实际应用中不建议用于保护敏感信息。
    - 多重加密和CBC模式可提升加密强度，但依然不如标准AES等成熟算法。

## 常见问题与解决方法

### 1. 密钥和明文的位数不匹配怎么办？

**解决方法**：
- 确保密钥和明文的位数符合要求：
    - **基本加密与解密**：明文为8位二进制或ASCII字符，密钥为10位或16位二进制。
    - **多重加密**：密钥长度为10位的整数倍。
    - **CBC模式**：明文长度为10位的倍数，密钥长度为10位或其倍数。

### 2. 如何生成随机密钥和初始向量？

**解决方法**：
- 使用Java的`SecureRandom`类生成随机二进制字符串。

```java
import java.security.SecureRandom;

public static String generateRandomBits(int length) {
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder();
    for(int i=0; i < length; i++){
        sb.append(random.nextBoolean() ? '1' : '0');
    }
    return sb.toString();
}

// 使用示例
String randomKey = generateRandomBits(10); // 生成10位随机密钥
String randomIV = generateRandomBits(10);  // 生成10位随机IV
```

### 3. 如何处理ASCII字符转换中的溢出问题？

**解决方法**：
- 在`asciiEncrypt`方法中，每个字符被转换为8位二进制字符串。确保输入字符的ASCII码在0-255范围内。

### 4. CBC模式下，如何安全传输初始向量（IV）？

**解决方法**：
- 初始向量（IV）不需要保密，但必须唯一且随机。
- 可以将IV与密文一起传输，例如将IV附加在密文的开头部分，解密时首先提取IV。

### 5. 为什么解密后的明文与原文不一致？

**解决方法**：
- **检查密钥**：确保加密和解密时使用相同的密钥。
- **检查IV**：在CBC模式下，加密和解密需使用相同的初始向量（IV）。
- **密文完整性**：确保密文在传输过程中未被篡改。
- **明文长度**：在CBC模式下，明文长度必须为10位的倍数，不足部分需右侧补零。

### 6. 暴力破解过程为何耗时较长？

**解决方法**：
- S-DES密钥空间为10位（1024种），暴力破解需要遍历所有可能的密钥，耗时取决于计算机性能。
- 建议在性能较好的机器上运行，或缩小测试范围。

## 结语

感谢您选择使用S-DES加解密工具。本工具旨在帮助您理解和实践简化的数据加密标准（S-DES）算法，通过直观的界面和丰富的功能，您可以轻松进行各种加密与解密操作。若在使用过程中遇到任何问题，欢迎参考本指南或联系开发者寻求帮助。

---

**开发者注**：本工具主要用于学习目的，未经过严格的安全性测试。请勿在生产环境中使用，以保护敏感信息的安全。