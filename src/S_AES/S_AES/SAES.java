package S_AES;

import java.util.*;

public class SAES {

    // 轮常数
    static String cons1 = "10000000";
    static String cons2 = "00110000";


    // S盒
    static String[][] S_Box = {
            {"9", "4", "A", "B"},
            {"D", "1", "8", "5"},
            {"6", "2", "0", "3"},
            {"C", "E", "F", "7"}
    };

    // 逆S盒
    static String[][] S_deBox = {
            {"A", "5", "9", "B"},
            {"1", "7", "8", "F"},
            {"6", "0", "2", "3"},
            {"C", "4", "D", "E"}
    };

    static String[][] mixMatrix = {{"1", "4"}, {"4", "1"}};
    static String[][] deMixMatrix = {{"9", "2"}, {"2", "9"}};

    // 将16位二进制字符串转换为状态矩阵
    static String[][] toStateMatrix(String bytetext) {
        String[][] matrix = {
                {bytetext.substring(0, 4), bytetext.substring(8, 12)},
                {bytetext.substring(4, 8), bytetext.substring(12, 16)}
        };
        return matrix;
    }

    // 将状态矩阵转换回16位二进制字符串
    static String reStateMatrix(String[][] matrix) {
        String byteText = matrix[0][0] + matrix[1][0] + matrix[0][1] + matrix[1][1];
        return byteText;
    }

    // 逐位异或操作
    static String xor(String text1, String text2) {
        StringBuilder resText = new StringBuilder();
        for (int i = 0; i < text1.length(); i++) {
            resText.append(text1.charAt(i) == text2.charAt(i) ? '0' : '1');
        }
        return resText.toString();
    }

    // 半字节替代
    static String halfByteReplace(String text, String[][] Box) {
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < text.length(); index += 4) {
            int i = Integer.parseInt(text.substring(index, index + 2), 2);
            int j = Integer.parseInt(text.substring(index + 2, index + 4), 2);
            result.append(Box[i][j]);
        }
        // 将十六进制字符串转换为二进制字符串
        return hexToBin(result.toString(), 16);
    }

    // 左循环移位
    static String leftShift(String text) {
        String[][] stateMatrix = toStateMatrix(text);
        // 交换
        String temp = stateMatrix[1][0];
        stateMatrix[1][0] = stateMatrix[1][1];
        stateMatrix[1][1] = temp;
        return reStateMatrix(stateMatrix);
    }

    // GF(2^4)上的加法（异或）
    static int gfAdd(int a, int b) {
        return a ^ b;
    }

    // GF(2^4)上的乘法
    static int gfMultiply(int a, int b) {
        int result = 0;
        while (b != 0) {
            if ((b & 1) != 0)
                result ^= a;
            a <<= 1;
            if ((a & 0x10) != 0)
                a ^= 0b10011; // 不可约多项式 x^4 + x + 1
            b >>= 1;
        }
        return result % 16; // 结果确保为4位
    }

    // 列混淆操作
    static String colMix(String[][] colMatrix, String byteText) {
        String[][] myMatrix = toStateMatrix(byteText);
        String[][] resMatrix = new String[2][2];

        resMatrix[0][0] = intToBin(
                gfAdd(
                        gfMultiply(Integer.parseInt(colMatrix[0][0], 16), Integer.parseInt(myMatrix[0][0], 2)),
                        gfMultiply(Integer.parseInt(colMatrix[0][1], 16), Integer.parseInt(myMatrix[1][0], 2))
                ), 4);

        resMatrix[0][1] = intToBin(
                gfAdd(
                        gfMultiply(Integer.parseInt(colMatrix[0][0], 16), Integer.parseInt(myMatrix[0][1], 2)),
                        gfMultiply(Integer.parseInt(colMatrix[0][1], 16), Integer.parseInt(myMatrix[1][1], 2))
                ), 4);

        resMatrix[1][0] = intToBin(
                gfAdd(
                        gfMultiply(Integer.parseInt(colMatrix[1][0], 16), Integer.parseInt(myMatrix[0][0], 2)),
                        gfMultiply(Integer.parseInt(colMatrix[1][1], 16), Integer.parseInt(myMatrix[1][0], 2))
                ), 4);

        resMatrix[1][1] = intToBin(
                gfAdd(
                        gfMultiply(Integer.parseInt(colMatrix[1][0], 16), Integer.parseInt(myMatrix[0][1], 2)),
                        gfMultiply(Integer.parseInt(colMatrix[1][1], 16), Integer.parseInt(myMatrix[1][1], 2))
                ), 4);

        return reStateMatrix(resMatrix);
    }

    // 密钥扩展的G函数
    static String G(String byteText, String cons, String[][] Box) {
        // 左循环移位
        String resText = byteText.substring(4) + byteText.substring(0, 4);

        // S盒替代
        StringBuilder result = new StringBuilder();
        for (int index = 0; index < resText.length(); index += 4) {
            int i = Integer.parseInt(resText.substring(index, index + 2), 2);
            int j = Integer.parseInt(resText.substring(index + 2, index + 4), 2);
            result.append(Box[i][j]);
        }
        // 将十六进制字符串转换为二进制字符串
        result = new StringBuilder(hexToBin(result.toString(), 8));

        // 与轮常数异或
        return xor(result.toString(), cons);
    }

    // 密钥扩展函数
    static String expandKey(String key, String cons, String[][] Box) {
        String rightKey = key.substring(8);
        String leftKey = key.substring(0, 8);

        // 新的左半部分
        String resLeft = xor(G(rightKey, cons, Box), leftKey);

        // 新的右半部分
        String resRight = xor(resLeft, rightKey);

        return resLeft + resRight;
    }

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

    // ASCII加密：传入一个字符串和密钥，返回密文列表
    static String asciiEncrypt(String str, String key) {
        String ciphertext = "";
        for (char c : str.toCharArray()) {
            int asciiCode = (int) c;
            String binaryString = padLeftZeros(Integer.toBinaryString(asciiCode), 16);
            String encrypted = multiplyEncrypt(binaryString, key);
            ciphertext+=(char) Integer.parseInt(encrypted,2);
        }
        return ciphertext;
    }

    // ASCII解密：传入密文列表和密钥，返回解密后的字符串
    static String asciiDecrypt(String ciphertext, String key) {
        String plaintext = "";
        for (char c : ciphertext.toCharArray()) {
            int asciiCode = (int) c;
            String binaryString = padLeftZeros(Integer.toBinaryString(asciiCode), 16);
            String decrypted = multiplyDecrypt(binaryString, key);
            plaintext+=(char) Integer.parseInt(decrypted,2);
        }
        return plaintext;
    }

    // 多重加密：传入明文和密钥（长度为16的倍数），返回密文
    static String multiplyEncrypt(String plainText, String key) {
        int level = key.length() / 16;
        String cyberText = plainText;
        for (int i = 0; i < level; i++) {
            String temKey = key.substring(0, 16);
            key = key.substring(16);
            cyberText = enCrypt(cyberText, temKey);
        }
        return cyberText;
    }

    // 多重解密：传入密文和密钥（长度为16的倍数），返回明文
    // 同理，三重加密使用48bits(K1+K2+K3)的模式进行三重加解密。
    static String multiplyDecrypt(String cyberText, String key) {
        int level = key.length() / 16;
        String plainText = cyberText;
        for (int i = 0; i < level; i++) {
            String temKey = key.substring(key.length() - 16);
            key = key.substring(0, key.length() - 16);
            plainText = deCrypt(plainText, temKey);
        }
        return plainText;
    }

    // 中间相遇攻击：输入明文和密文，尝试找到对应的密钥k1+k2
    static List<String> breakOut(String plainText, String cipherText) {
        long startTime = System.currentTimeMillis();
        Map<String, Integer> encryptMap = new HashMap<>();
        Map<String, Integer> decryptMap = new HashMap<>();

        // 遍历所有可能的K1，生成中间密文
        for (int k1 = 0; k1 <= 0xFFFF; k1++) {
            String key1 = padLeftZeros(Integer.toBinaryString(k1), 16);
            String intermediateCipher = enCrypt(plainText, key1);
            encryptMap.put(intermediateCipher, k1);
        }

        // 遍历所有可能的K2，生成中间明文
        for (int k2 = 0; k2 <= 0xFFFF; k2++) {
            String key2 = padLeftZeros(Integer.toBinaryString(k2), 16);
            String intermediatePlain = deCrypt(cipherText, key2);
            decryptMap.put(intermediatePlain, k2);
        }

        // 寻找匹配的中间值
        List<String> keysFound = new ArrayList<>();
        for (String intermediate : encryptMap.keySet()) {
            if (decryptMap.containsKey(intermediate)) {
                int k1 = encryptMap.get(intermediate);
                int k2 = decryptMap.get(intermediate);
                String key1 = padLeftZeros(Integer.toBinaryString(k1), 16);
                String key2 = padLeftZeros(Integer.toBinaryString(k2), 16);
                String fullKey = key1 + key2;
                keysFound.add(fullKey);
            }
        }

        long endTime = System.currentTimeMillis();
        double timeTaken = (endTime - startTime) / 1000.0;
        //System.out.println("找到的密钥列表：" + keysFound);
        //System.out.println("用时：" + timeTaken + " 秒");
        return keysFound;
    }
    // CBC模式加解密
    // CBC模式加密：传入明文、密钥、初始向量，返回密文列表
    static String cbcEncrypt(String plaintext, String key, String iv) {
        String cipherBlocks = "";
        // 确保明文长度为16的倍数
        int blockSize = 16;
        int totalBlocks = (plaintext.length() + blockSize - 1) / blockSize;
        String previousCipher = iv;
        for (int i = 0; i < totalBlocks; i++) {
            int start = i * blockSize;
            int end = Math.min((i + 1) * blockSize, plaintext.length());
            String block = plaintext.substring(start, end);
            // 如果不足16位，右侧补零
            block = padRightZeros(block, blockSize);
            String xored = xor(block, previousCipher);
            String cipherBlock = enCrypt(xored, key);
            cipherBlocks+=cipherBlock;
            previousCipher = cipherBlock;
        }
        return cipherBlocks;
    }

    // CBC模式解密：传入密文列表、密钥、初始向量，返回明文
    static String cbcDecrypt(String cipherBlocks, String key, String iv) {
        String plainBlocks = "";
        // 确保明文长度为16的倍数
        int blockSize = 16;
        int totalBlocks = (cipherBlocks.length() + blockSize - 1) / blockSize;
        String previousCipher = iv;
        for (int i = 0; i < totalBlocks; i++) {
            int start = i * blockSize;
            int end = Math.min((i + 1) * blockSize, cipherBlocks.length());
            String block = cipherBlocks.substring(start, end);
            // 如果不足16位，右侧补零
            block = padRightZeros(block, blockSize);
            String decryptedBlock = deCrypt(block, key);
            String plainBlock = xor(decryptedBlock, previousCipher);
            plainBlocks+=plainBlock;
            previousCipher = block;

        }
        return plainBlocks;
    }

    // 辅助方法：将十六进制字符串转换为指定长度的二进制字符串
    static String hexToBin(String hex, int bits) {
        int num = Integer.parseInt(hex, 16);
        return padLeftZeros(Integer.toBinaryString(num), bits);
    }

    // 辅助方法：将整数转换为指定长度的二进制字符串
    static String intToBin(int num, int bits) {
        return padLeftZeros(Integer.toBinaryString(num), bits);
    }

    // 辅助方法：左侧补零
    static String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length)
            return inputString;
        StringBuilder sb = new StringBuilder();
        while (sb.length() + inputString.length() < length) {
            sb.append('0');
        }
        sb.append(inputString);
        return sb.toString();
    }

    // 辅助方法：右侧补零
    static String padRightZeros(String inputString, int length) {
        if (inputString.length() >= length)
            return inputString;
        StringBuilder sb = new StringBuilder(inputString);
        while (sb.length() < length) {
            sb.append('0');
        }
        return sb.toString();
    }

    // 主函数用于测试
    public static void main(String[] args) {
        // 测试16Bit二进制加密解密
        // 加密
        String plainText = "0101010101010101";
        String key_bit = "1010101010101010";
        String cipherText = enCrypt(plainText, key_bit);
        System.out.println("测试16Bit二进制加密解密:");
        System.out.println("明文为："+plainText);
        System.out.println("密钥为："+key_bit);
        System.out.println("加密后的密文为: " + cipherText);

        // 解密
        String decryptedText = deCrypt(cipherText, key_bit);
        System.out.println("解密后的明文为: " + decryptedText);


        // 测试ASCII加密解密
        String message = "Hello";
        String key_ascii = "10100111001110111010011100111011"; // 32位密钥，双重加密
        System.out.println("测试ASCII加密解密:");
        System.out.println("原始消息：" + message);

        // 加密
        String cipherList = asciiEncrypt(message, key_ascii);
        System.out.println("加密后的密文列表：" + cipherList);

        // 解密
        String decryptedMessage = asciiDecrypt(cipherList, key_ascii);
        System.out.println("解密后的消息：" + decryptedMessage);

        // 测试多重加密和解密
        //这里用和测试16bit一样的明文
        //String plainText = "0110111101101011";
        String multiKey = "11110010010010010010100010000110"; // 32位密钥，双重加密
        String multiCipher = multiplyEncrypt(plainText, multiKey);
        System.out.println("测试多重加密和解密:");
        System.out.println("多重加密后的密文：" + multiCipher);

        String multiDecrypted = multiplyDecrypt(multiCipher, multiKey);
        System.out.println("多重解密后的明文：" + multiDecrypted);

        // 测试中间相遇攻击
        System.out.println("开始进行中间相遇攻击查找密钥...");
        breakOut(plainText, cipherText);

        // 测试CBC模式
        String longPlainText = "01101111011010110110111101101011"; // 长度32位
        String cbcKey = "1010011100111011"; // 16位密钥
        String iv = "1100101011110101"; // 初始向量
        String cbcCipherList = cbcEncrypt(longPlainText, cbcKey, iv);
        System.out.println("使用CBC模式加密的明文:"+longPlainText);
        System.out.println("CBC加密后的密文列表：" + cbcCipherList);

        String cbcDecryptedText = cbcDecrypt(cbcCipherList, cbcKey, iv);
        System.out.println("CBC解密后的明文：" + cbcDecryptedText);

        // 篡改密文测试
        String modified_keyblock = "0000000000000000";
        int location = 1;
        System.out.println("篡改第"+location+"个密文块，"+"篡改内容为："+modified_keyblock);
        //cbcCipherList.set(location, modified_keyblock); // 篡改第二个密文块
        String tamperedDecryptedText = cbcDecrypt(cbcCipherList, cbcKey, iv);
        System.out.println("篡改密文后的解密结果：" + tamperedDecryptedText);
 }
}


