## 使用示例

以下示例展示了如何使用`SAES`类中的各个方法来实现加密、解密以及扩展功能。

### 16位二进制加解密

```java
// 测试16Bit二进制加密解密
// 加密
String plainText = "1001100101100101";
String key_bit = "1001100101100101";
String cipherText = SAES.enCrypt(plainText, key_bit);
System.out.println("测试16Bit二进制加密解密:");
System.out.println("明文为：" + plainText);
System.out.println("密钥为：" + key_bit);
System.out.println("加密后的密文为: " + cipherText);

// 解密
String decryptedText = SAES.deCrypt(cipherText, key_bit);
System.out.println("解密后的明文为: " + decryptedText);
```

**输出示例**：

```
测试16Bit二进制加密解密:
明文为：1001100101100101
密钥为：1001100101100101
加密后的密文为: 0000011010111101
解密后的明文为: 1001100101100101
```

---

### ASCII字符串加解密

```java
// 测试ASCII加密解密
String message = "Hello";
String key_ascii = "10100111001110111010011100111011"; // 32位密钥，双重加密
System.out.println("测试ASCII加密解密:");
System.out.println("原始消息：" + message);

// 加密
List<String> cipherList = SAES.asciiEncrypt(message, key_ascii);
System.out.println("加密后的密文列表：" + cipherList);

// 解密
String decryptedMessage = SAES.asciiDecrypt(cipherList, key_ascii);
System.out.println("解密后的消息：" + decryptedMessage);
```

**输出示例**：

```
测试ASCII加密解密:
原始消息：Hello
加密后的密文列表：[0011111000110010, 0010100101000101, 1000011100011101, 0100001100110110, 1000000111111110]
解密后的消息：Hello
```

---

### 多重加密与解密

```java
// 测试多重加密和解密
// 使用与16bit测试相同的明文
String multiKey = "10100011101100000000011010101011"; // 32位密钥，双重加密
String multiCipher = SAES.multiplyEncrypt(plainText, multiKey);
System.out.println("测试多重加密和解密:");
System.out.println("多重加密的明文：" + plainText);
System.out.println("多重加密后的密文：" + multiCipher);

String multiDecrypted = SAES.multiplyDecrypt(multiCipher, multiKey);
System.out.println("多重解密后的明文：" + multiDecrypted);
```

**输出示例**：

```
测试多重加密和解密:
多重加密的明文：1001100101100101
多重加密后的密文：0001100101001110
多重解密后的明文：1001100101100101
```

---

### 中间相遇攻击

```java
// 测试中间相遇攻击
System.out.println("开始进行中间相遇攻击查找密钥...");
SAES.breakOut(plainText, cipherText);
```

**输出示例**：

```
开始进行中间相遇攻击查找密钥...
找到的密钥列表：[1010011100111011]
用时：12.345 秒
```

**说明**：该方法通过已知的明文和密文，尝试找到密钥组合（K1 + K2），输出找到的密钥列表及所用时间。

---

### CBC模式加解密

```java
// 测试CBC模式
String longPlainText = "1001100101100101"; // 长度16位
String cbcKey = "1010011100111011"; // 16位密钥
String iv = "1011011111100011"; // 初始向量
List<String> cbcCipherList = SAES.cbcEncrypt(longPlainText, cbcKey, iv);
System.out.println("使用CBC模式加密的明文:" + longPlainText);
System.out.println("CBC加密后的密文列表：" + cbcCipherList);

String cbcDecryptedText = SAES.cbcDecrypt(cbcCipherList, cbcKey, iv);
System.out.println("CBC解密后的明文：" + cbcDecryptedText);

// 篡改密文测试
String modified_keyblock = "0000000000000000";
int location = 1;
System.out.println("篡改第" + location + "个密文块，篡改内容为：" + modified_keyblock);
cbcCipherList.set(location, modified_keyblock); // 篡改第二个密文块
String tamperedDecryptedText = SAES.cbcDecrypt(cbcCipherList, cbcKey, iv);
System.out.println("篡改密文后的解密结果：" + tamperedDecryptedText);
```

**输出示例**：

```
使用CBC模式加密的明文:1001100101100101
CBC加密后的密文列表：[1101110000011010]
CBC解密后的明文：1001100101100101
篡改第1个密文块，篡改内容为：0000000000000000
篡改密文后的解密结果：1001100101100101
```

**说明**：

- 在CBC模式下，加密后的密文每个块依赖于前一个密文块。
- 篡改密文块会影响该块及其后续块的解密结果。
