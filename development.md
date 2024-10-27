# S-AES 加解密 Java 实现开发手册

## 目录

1. [简介](#简介)
2. [环境要求](#环境要求)
3. [安装与配置](#安装与配置)
4. [类结构与组件](#类结构与组件)
5. [API接口文档](#API接口文档)
    - [类：SAES](#类-saes)
        - [方法列表](#方法列表)
7. [注意事项](#注意事项)
8. [常见问题](#常见问题)
9. [参考资料](#参考资料)

---

## 简介

本开发手册旨在详细介绍基于Java实现的简化高级加密标准（S-AES）算法。S-AES是一种轻量级加密算法，适用于资源受限的环境。该实现包括基本的加密与解密功能，以及扩展功能如ASCII编码处理、多重加密、中间相遇攻击和密码分组链接（CBC）模式。

**S-AES算法流程：**

![img](img\S-AES算法流程图.png?lastModify=1729486971)

**S-AES使用的S盒：**

![img](img\S盒.png?lastModify=1729486971)

## 项目结构

```
├── picture                         - 存放结果数据和图片
├── src
│    ├── S_AES
│    │    ├── SAES.java            - SAES算法实现
│    │    └── SAESGUI.java          - 界面设计   
└── README.md                       - 包含开发手册、用户指南、测试结果
```



## 环境要求

- **Java版本**：Java SE 8或更高版本
- **开发工具**：推荐使用Eclipse、IntelliJ IDEA或其他支持Java的集成开发环境（IDE）

## 安装与配置

1. **下载源码**：将提供的`SAES.java`文件下载至本地开发环境中。
2. **创建项目**：
    - 使用您的IDE创建一个新的Java项目。
    - 将`SAES.java`文件添加到项目的源代码目录中。
3. **编译项目**：
    - 确保Java环境已正确配置。
    - 在IDE中编译项目，解决可能的依赖问题。
4. **运行测试**：
    - 运行`SAES`类中的`main`方法，观察控制台输出以验证功能是否正常。

## 类结构与组件

### 类：SAES

`SAES`类包含实现S-AES加密与解密的所有方法，包括扩展功能如ASCII处理、多重加密、暴力破解和CBC模式。所有方法均为静态方法，方便直接调用。

## API接口文档

### 类：SAES

```java
public class SAES {
    // 静态方法列表
    static String enCrypt(String plainText, String key);
    static String deCrypt(String cipherText, String key);
    static String asciiEncrypt(String str, String key);
    static String asciiDecrypt(String cyberList, String key);
    static String multiplyEncrypt(String plainText, String key);
    static String multiplyDecrypt(String cyberText, String key);
    static List<String> breakOut(String plainText, String cipherText);
    static String cbcEncrypt(String plaintext, String key, String iv);
    static String cbcDecrypt(String cipherBlocks, String key, String iv);
    // 辅助方法
    static String hexToBin(String hex, int bits);
    static String intToBin(int num, int bits);
    static String padLeftZeros(String inputString, int length);
    static String padRightZeros(String inputString, int length);
    static String[][] toStateMatrix(String bytetext);
    static String reStateMatrix(String[][] matrix);
    static String xor(String text1, String text2);
    static String halfByteReplace(String text, String[][] Box);
    static String leftShift(String text);
    static int gfAdd(int a, int b);
    static int gfMultiply(int a, int b);
    static String colMix(String[][] colMatrix, String byteText);
    static String G(String byteText, String cons, String[][] Box);
    static String expandKey(String key, String cons, String[][] Box);
}
```

#### 方法列表

以下是`SAES`类中各方法的详细说明：

---

#### `static String enCrypt(String plainText, String key)`

**描述**：使用给定的16位密钥对16位明文进行加密，返回16位密文。

**参数**：
- `plainText`：明文二进制字符串（16位）。
- `key`：密钥二进制字符串（16位）。

**返回**：

- 加密后的密文二进制字符串（16位）。

以下是加密函数的具体实现：

```
// 加密函数
    static String enCrypt(String plainText, String key) {

        plainText = padLeftZeros(plainText, 16);
        key = padLeftZeros(key, 16);

        // 密钥扩展
        String[] keyList = new String[3];
        keyList[0] = key;
        keyList[1] = expandKey(key, cons1, S_Box);
        keyList[2] = expandKey(keyList[1], cons2, S_Box);

        // 第0轮：轮密钥加
        String temText = xor(plainText, keyList[0]);

        // 第1轮
        temText = halfByteReplace(temText, S_Box); // 半字节替代
        temText = leftShift(temText);              // 行移位
        temText = colMix(mixMatrix, temText);      // 列混淆
        temText = xor(temText, keyList[1]);        // 轮密钥加

        // 第2轮
        temText = halfByteReplace(temText, S_Box); // 半字节替代
        temText = leftShift(temText);              // 行移位
        temText = xor(temText, keyList[2]);        // 轮密钥加

        return temText;
    }
```



---

#### `static String deCrypt(String cipherText, String key)`

**描述**：使用给定的16位密钥对16位密文进行解密，返回16位明文。

**参数**：
- `cipherText`：密文二进制字符串（16位）。
- `key`：密钥二进制字符串（16位）。

**返回**：
- 解密后的明文二进制字符串（16位）。

以下是解密函数的具体实现：

```
// 解密函数
static String deCrypt(String cipherText, String key) {
    cipherText = padLeftZeros(cipherText, 16);
    key = padLeftZeros(key, 16);

    // 密钥扩展
    String[] keyList = new String[3];
    keyList[0] = key;
    keyList[1] = expandKey(key, cons1, S_Box);
    keyList[2] = expandKey(keyList[1], cons2, S_Box);

    // 第0轮：轮密钥加
    String temText = xor(cipherText, keyList[2]);

    // 第1轮
    temText = leftShift(temText);              // 逆行移位
    temText = halfByteReplace(temText, S_deBox); // 逆半字节替代
    temText = xor(temText, keyList[1]);        // 轮密钥加
    temText = colMix(deMixMatrix, temText);    // 逆列混淆

    // 第2轮
    temText = leftShift(temText);              // 逆行移位
    temText = halfByteReplace(temText, S_deBox); // 逆半字节替代
    temText = xor(temText, keyList[0]);        // 轮密钥加

    return temText;
}
```

---

#### `static String asciiEncrypt(String str, String key)`

**描述**：将输入的ASCII字符串分字符加密，每个字符使用16位二进制表示，返回加密后的密文字符串。

**参数**：
- `str`：要加密的ASCII字符串。
- `key`：密钥二进制字符串（长度为16的倍数，例如32位表示双重加密）。

**返回**：
- 加密后的密文字符串。

---

#### `static String asciiDecrypt(String cyberList, String key)`

**描述**：对加密后的密文字符串进行解密，恢复为原始ASCII字符串。

**参数**：
- `cyberList`：密文字符串。
- `key`：密钥二进制字符串（长度为16的倍数，例如32位表示双重解密）。

**返回**：
- 解密后的ASCII字符串。

---

#### `static String multiplyEncrypt(String plainText, String key)`

**描述**：对16位明文使用多重加密（密钥长度为16的倍数）进行加密。

**参数**：
- `plainText`：明文二进制字符串（16位）。
- `key`：密钥二进制字符串（长度为16的倍数，例如32位表示双重加密）。

**返回**：
- 多重加密后的密文二进制字符串。

---

#### `static String multiplyDecrypt(String cyberText, String key)`

**描述**：对多重加密后的密文进行解密，恢复为原始16位明文。

**参数**：
- `cyberText`：多重加密后的密文二进制字符串。
- `key`：密钥二进制字符串（长度为16的倍数，例如32位表示双重解密）。

**返回**：
- 解密后的明文二进制字符串。

---

#### `static List<String> breakOut(String plainText, String cipherText)`

**描述**：实现中间相遇攻击，通过已知的明文和密文尝试找到对应的密钥组合（K1 + K2）。

**参数**：
- `plainText`：已知的明文二进制字符串（16位）。
- `cipherText`：已知的密文二进制字符串（16位）。

**返回**：
- 返回密钥列表。

---

#### `static String cbcEncrypt(String plaintext, String key, String iv)`

**描述**：使用CBC模式对较长的明文进行加密。明文按16位分块，初始向量（IV）为16位二进制字符串。

**参数**：
- `plaintext`：明文二进制字符串（长度为16的倍数，不足部分右侧补零）。
- `key`：密钥二进制字符串（16位）。
- `iv`：初始向量二进制字符串（16位）。

**返回**：
- 加密后的密文二进制字符串。

---

#### `static String cbcDecrypt(String cipherBlocks, String key, String iv)`

**描述**：使用CBC模式对密文列表进行解密，恢复为原始明文。

**参数**：
- `cipherBlocks`：密文二进制字符串。
- `key`：密钥二进制字符串（16位）。
- `iv`：初始向量二进制字符串（16位）。

**返回**：
- 解密后的明文二进制字符串。

---

#### 辅助方法

以下方法为内部实现提供支持，通常不需要直接调用。

---

#### `static String hexToBin(String hex, int bits)`

**描述**：将十六进制字符串转换为指定长度的二进制字符串，不足部分左侧补零。

**参数**：
- `hex`：输入的十六进制字符串。
- `bits`：目标二进制字符串的长度。

**返回**：
- 转换后的二进制字符串。

---

#### `static String intToBin(int num, int bits)`

**描述**：将整数转换为指定长度的二进制字符串，不足部分左侧补零。

**参数**：
- `num`：要转换的整数。
- `bits`：目标二进制字符串的长度。

**返回**：
- 转换后的二进制字符串。

---

#### `static String padLeftZeros(String inputString, int length)`

**描述**：在字符串左侧补零，使其达到指定长度。

**参数**：
- `inputString`：原始字符串。
- `length`：目标长度。

**返回**：
- 补零后的字符串。

---

#### `static String padRightZeros(String inputString, int length)`

**描述**：在字符串右侧补零，使其达到指定长度。

**参数**：
- `inputString`：原始字符串。
- `length`：目标长度。

**返回**：
- 补零后的字符串。

---

#### `static String[][] toStateMatrix(String bytetext)`

**描述**：将16位二进制字符串转换为2x2的状态矩阵。

**参数**：
- `bytetext`：16位二进制字符串。

**返回**：
- 2x2状态矩阵（二维字符串数组）。

---

#### `static String reStateMatrix(String[][] matrix)`

**描述**：将2x2的状态矩阵转换回16位二进制字符串。

**参数**：
- `matrix`：2x2状态矩阵（二维字符串数组）。

**返回**：
- 16位二进制字符串。

---

#### `static String xor(String text1, String text2)`

**描述**：对两个相同长度的二进制字符串逐位进行异或操作。

**参数**：
- `text1`：第一个二进制字符串。
- `text2`：第二个二进制字符串。

**返回**：
- 异或结果的二进制字符串。

---

#### `static String halfByteReplace(String text, String[][] Box)`

**描述**：对二进制字符串进行半字节替代，使用指定的S盒。

**参数**：
- `text`：二进制字符串。
- `Box`：S盒或逆S盒（二维字符串数组）。

**返回**：
- 替代后的二进制字符串。

---

#### `static String leftShift(String text)`

**描述**：对状态矩阵进行左循环移位（行移位）。

**参数**：
- `text`：16位二进制字符串。

**返回**：
- 移位后的16位二进制字符串。

---

#### `static int gfAdd(int a, int b)`

**描述**：在GF(2^4)上进行加法（即异或操作）。

**参数**：
- `a`：第一个元素。
- `b`：第二个元素。

**返回**：
- 加法结果。

---

#### `static int gfMultiply(int a, int b)`

**描述**：在GF(2^4)上进行乘法运算。

**参数**：
- `a`：第一个元素。
- `b`：第二个元素。

**返回**：
- 乘法结果。

---

#### `static String colMix(String[][] colMatrix, String byteText)`

**描述**：进行列混淆操作，使用指定的混淆矩阵。

**参数**：
- `colMatrix`：列混淆矩阵（二维字符串数组）。
- `byteText`：16位二进制字符串。

**返回**：
- 混淆后的16位二进制字符串。

---

#### `static String G(String byteText, String cons, String[][] Box)`

**描述**：密钥扩展的G函数，进行左循环移位、S盒替代和与轮常数异或操作。

**参数**：
- `byteText`：8位二进制字符串（密钥的右半部分）。
- `cons`：轮常数二进制字符串。
- `Box`：S盒或逆S盒。

**返回**：
- 经过G函数处理后的8位二进制字符串。

---

#### `static String expandKey(String key, String cons, String[][] Box)`

**描述**：进行密钥扩展，生成下一个轮的密钥。

**参数**：
- `key`：当前轮的密钥二进制字符串（16位）。
- `cons`：轮常数二进制字符串。
- `Box`：S盒或逆S盒。

**返回**：

- 扩展后的下一个轮密钥二进制字符串（16位）。



---

## 注意事项

1. **密钥长度**：
    - 基本加密与解密方法要求密钥为16位二进制字符串。
    - 多重加密与解密支持密钥长度为16的倍数，如32位（双重加密）、48位（三重加密）等。

2. **明文与密文长度**：
    - 加密方法要求明文为16位二进制字符串。
    - 对于ASCII加密，每个字符被转换为16位二进制进行加密。

3. **多重加密**：
    - 密钥的长度应为16位的整数倍。
    - 加密过程从密钥的前16位开始，逐步使用剩余密钥进行多轮加密。

4. **中间相遇攻击**：
    - 该攻击方法适用于双重加密，尝试找到两个密钥K1和K2，使得`enCrypt(enCrypt(plainText, K1), K2) == cipherText`。
    - 由于密钥空间较大（2^16），中间相遇攻击可能耗时较长。

5. **CBC模式**：
    - 初始向量（IV）必须为16位二进制字符串，并且加密与解密双方必须共享相同的IV。
    - 在CBC模式下，加密明文长度必须为16的倍数，不足部分需右侧补零。


---

## 常见问题

### 1. **密钥和明文的位数不匹配怎么办？**

确保密钥和明文均为16位二进制字符串。对于多重加密，密钥长度应为16的倍数。例如，双重加密需使用32位密钥。

### 2. **如何生成随机密钥和初始向量？**

可以使用Java的`Random`类或`SecureRandom`类生成随机的二进制字符串。例如：

```java
import java.security.SecureRandom;

public static String generateRandomBits(int length) {
    SecureRandom random = new SecureRandom();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < length; i++) {
        sb.append(random.nextBoolean() ? '1' : '0');
    }
    return sb.toString();
}

// 使用示例
String randomKey = generateRandomBits(16); // 生成16位随机密钥
String randomIV = generateRandomBits(16);  // 生成16位随机IV
```

### 3. **如何处理ASCII字符转换中的溢出问题？**

在`asciiEncrypt`方法中，每个字符被转换为16位二进制字符串。如果字符的ASCII码超过8位（即超过255），需要确保处理正确。通常，ASCII码在0-255范围内，可以安全转换为16位二进制。



---

## 参考资料

**S-AES算法介绍**：

- [S-AES算法介绍](img/S-AES.pdf)

**密码学基础**：

- [Understanding CBC Mode](https://en.wikipedia.org/wiki/Block_cipher_mode_of_operation#Cipher_Block_Chaining_(CBC))
- [Meet-in-the-Middle Attack](https://en.wikipedia.org/wiki/Meet-in-the-middle_attack)

---

**开发者注**：本手册旨在帮助开发者理解和使用`SAES`类进行加密与解密操作。建议在实际应用中结合更严格的安全措施，如密钥管理、输入验证等，以确保系统的安全性。

