package com.mart.mymartbee.algorithm;

import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.BASE64EncoderStream;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class TripleDes {

    private static Cipher ecipher;
    private static Cipher dcipher;

    public static String getDESEncryptValue(String strValue, String strKeyValue) {
        // sblw-3hn8-sqoy19
        String encrypted = "";
        try {
            // generate secret key using DES algorithm
            String mykey = strKeyValue; // 128 bit key
            Key aesKey = new SecretKeySpec(mykey.getBytes(), "DES");
            ecipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");

            // initialize the ciphers with the given key
            ecipher.init(Cipher.ENCRYPT_MODE, aesKey);
            encrypted = encrypt(strValue);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
            return "No Such Algorithm:" + e.getMessage();
        } catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());
            return "No Such Padding:" + e.getMessage();
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
            return "Invalid Key:" + e.getMessage();
        }

        return encrypted;
    }

    public static String getDESDecryptValue(String encryptedValue, String strKeyValue) {
        // sblw-3hn8-sqoy19
        String decrypted = "";
        try {
            // generate secret key using DES algorithm
            String mykey = strKeyValue;
            Key aesKey = new SecretKeySpec(mykey.getBytes(), "DES");

            dcipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
            dcipher.init(Cipher.DECRYPT_MODE, aesKey);

            decrypted = decrypt(encryptedValue);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("No Such Algorithm:" + e.getMessage());
            return "No Such Algorithm:" + e.getMessage();
        } catch (NoSuchPaddingException e) {
            System.out.println("No Such Padding:" + e.getMessage());
            return "No Such Padding:" + e.getMessage();
        } catch (InvalidKeyException e) {
            System.out.println("Invalid Key:" + e.getMessage());
            return "Invalid Key:" + e.getMessage();
        }
        return decrypted;
    }

    public static String encrypt(String str) {
        try {
            // encode the string into a sequence of bytes using the named charset
            // storing the result into a new byte array.
            byte[] utf8 = str.getBytes("UTF8");
            byte[] enc = ecipher.doFinal(utf8);
            // encode to base64
            enc = BASE64EncoderStream.encode(enc);
            return new String(enc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String str) {
        try {
            // decode with base64 to get bytes
            byte[] dec = BASE64DecoderStream.decode(str.getBytes());
            byte[] utf8 = dcipher.doFinal(dec);
            // create new string based on the specified charset
            return new String(utf8, "UTF8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String generateRandom() {
        Random rand = new Random();

        // Generate random integers in range 0 to 999
        int rand_int1 = rand.nextInt(1000000000);
        int rand_int2 = rand.nextInt(1000000000);

        return "AN" + rand_int1 + "" + rand_int2;

    }
}
