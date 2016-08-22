package com.rontseng.util;

import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class AESUtil {
  private static Cipher ecipher;
  private static Cipher dcipher;
  private static SecretKey key = null;

  private static final String AES_KEY = "1234567890123456";

  static {
    key = new SecretKeySpec(AES_KEY.getBytes(), "AES");

    // Create an 8-byte initialization vector
    byte[] iv = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e,
                    0x0f };
    AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

    try {
      ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

      // CBC requires an initialization vector
      ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
      dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static String encrypt(String str) throws IllegalBlockSizeException, BadPaddingException {
    String encrypted = null;
    byte[] cipherText = ecipher.doFinal(str.getBytes());
    encrypted = new String(Base64.encodeBase64(cipherText));

    return encrypted;
  }

  public static String decrypt(String str) throws IllegalBlockSizeException, BadPaddingException {
    String decrypted = null;
    byte[] clearText = dcipher.doFinal(Base64.decodeBase64(str.getBytes()));
    decrypted = new String(clearText);

    return decrypted;
  }

  public static void main(String[] args) throws Exception {

    System.out.println(AESUtil.encrypt("newvin"));

    System.out.println(AESUtil.decrypt("q813fyeL+VnVn1mCGRY/0w=="));
  }
} // end class AES
