package excelDemo;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StopWatch;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java版3DES加密解密，适用于PHP版3DES加密解密(PHP语言开发的MCRYPT_3DES算法、MCRYPT_MODE_ECB模式、PKCS7填充方式)
 */
public class Mcrypt3Des {
    // key对象
    private static SecretKey secretKey = null;
    // 私鈅加密对象Cipher
    private static Cipher encryptCipher = null;

    private static Cipher decryptCipher = null;

    // 密钥
    private static String keyString = "111111111122222222223333";

    private static Log log = LogFactory.getLog(Mcrypt3Des.class);

    static {
        try {
            // 获得密钥
            secretKey = new SecretKeySpec(keyString.getBytes(), "DESede");
            
            /* 获得一个私鈅加密类Cipher，DESede是算法，ECB是加密模式，PKCS5Padding是填充方式 */
            encryptCipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            // 设置工作模式为加密模式，给出密钥
            encryptCipher.init(Cipher.ENCRYPT_MODE, secretKey);
            
            /* 获得一个私鈅加密类Cipher，DESede是算法，ECB是加密模式，PKCS5Padding是填充方式 */
            decryptCipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            // 设置工作模式为解密模式，给出密钥
            decryptCipher.init(Cipher.DECRYPT_MODE, secretKey);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 加密
     */
    public static String desEncrypt(String message) {
        // DES加密字符串
        String result;
        // 去掉换行符后的加密字符串
        String newResult = "";
        try {
            byte[] resultBytes = null;
            synchronized (Mcrypt3Des.class) {
                // 正式执行加密操作
                resultBytes = encryptCipher.doFinal(message.getBytes("UTF-8"));
            }
            // 进行BASE64编码
            result = Base64.encodeBase64String(resultBytes);
            // 去掉加密串中的换行符
            newResult = filter(result);
        } catch (Exception e) {
            log.error("加密" + message + "失败, " + e.getMessage());
        }
        return newResult;
    }


    /**
     * 解密
     */
    public static String desDecrypt(String message) {
        String result = "";
        try {
            // 进行BASE64编码
            byte[] messageBytes = Base64.decodeBase64(message);
            byte[] resultBytes = null;
            synchronized (Mcrypt3Des.class) {
                // 正式执行解密操作
                resultBytes = decryptCipher.doFinal(messageBytes);
            }
            result = new String(resultBytes, "UTF-8");
        } catch (Exception e) {
            log.error("解密" + message + "失败, " + e.getMessage());
        }
        return result;
    }


    public static String base64Decode(String message) {
        try {
            byte[] base64 = Base64.decodeBase64(message);
            return new String(base64);
        } catch (Exception e) {
            log.error("Base64解密" + message + "失败, " + e.getMessage());
            return "";
        }
    }

    public static String base64Encode(String message) {
        if (message == null) {
            return null;
        }
        byte[] base64 = Base64.encodeBase64(message.getBytes());
        return new String(base64);
    }

    /**
     * sha加密
     */
    public static String sha256Encode(String message) {
        String result = "";
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(message.getBytes());
            result = bytes2Hex(sha.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    public static String md5AndShaEncode(String message) {
        String result = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(message.getBytes());
            byte[] digestMd5 = md5.digest();

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            sha.update(digestMd5);
            result = bytes2Hex(sha.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return result;
    }

    /**
     * 二行制转字符串
     */
    private static String bytes2Hex(byte[] bts) {
        StringBuilder des = new StringBuilder();
        String tmp;
        for (byte bt : bts) {
            tmp = (Integer.toHexString(bt & 0xFF));
            if (tmp.length() == 1) {
                des.append("0");
            }
            des.append(tmp);
        }
        return des.toString().toUpperCase();
    }


    /**
     * 去掉加密字符串换行符
     */
    public static String filter(String str) {
        String output;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            int asc = str.charAt(i);
            if (asc != 10 && asc != 13) {
                sb.append(str.subSequence(i, i + 1));
            }
        }
        output = new String(sb);
        return output;
    }

    /**
     * 加密解密测试
     */
    public static void main(String[] args) {
        try {
            // String strText = "Hello world!";
            // String strText = "中文ABc123";
            // String strText = "abcd";
            String strText = "123";
            String deseResult = desEncrypt(strText);// 加密
            System.out.println("加密结果：" + deseResult);

            String desdResult = desDecrypt(deseResult);// 解密
            System.out.println("解密结果：" + desdResult);
            String s1 = base64Decode("emhvdXBlbmc=");
            System.out.println(s1);

            //System.out.println(sha256Encode("123456"));
            ExecutorService executorService = Executors.newFixedThreadPool(200);
            //ExecutorService executorService = Executors.newCachedThreadPool();
            final String phone = "17705812909";

            StopWatch stopWatch = new StopWatch();
            stopWatch.start();

            int len = 1000000;
            // 计数器类
            final CountDownLatch latch = new CountDownLatch(len);
            for (int i = 0; i < len; i++) {
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        String encrypt = Mcrypt3Des.desEncrypt(phone);
                        //Mcrypt3Des.desDecrypt(encrypt);
                        System.out.println("[加密前：" + phone + "], [加密后=" + encrypt + "], [解密后=" + Mcrypt3Des.desDecrypt(encrypt));
                        // 计数器减一
                        latch.countDown();
                    }
                });
            }
            // 等待所有的线程都处理完
            latch.await();
            // 关闭线程池
            executorService.shutdown();
            stopWatch.stop();
            System.out.println(stopWatch.getTotalTimeMillis() + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
