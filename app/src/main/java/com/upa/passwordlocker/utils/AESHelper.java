package com.upa.passwordlocker.utils;

import com.upa.passwordlocker.crypto.CryptoProvider;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESHelper {

    private static final String SEED = "r52h7T3s0791wyio";
    private static final String HEX = "0123456789ABCDEF";
    private static final String CIPHER_ALGORITHM = "AES";

    public static String encrypt(String cleartext) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {

        byte[] rawKey = getRawKey(SEED.getBytes());
        byte[] result = encrypt(rawKey, cleartext.getBytes());

        return toHex(result);
    }

    public static String decrypt(String encrypted) throws NoSuchAlgorithmException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchPaddingException {

        byte[] rawKey = getRawKey(SEED.getBytes());

        byte[] enc = toByte(encrypted);
        byte[] result = decrypt(rawKey, enc);

        return new String(result);
    }

    private static byte[] getRawKey(byte[] seed) throws NoSuchAlgorithmException {

        SecureRandom sr = SecureRandom.getInstance(EncryptionHelper.SALT_ALGORITHM, new CryptoProvider());
        sr.setSeed(seed);

        KeyGenerator generator = KeyGenerator.getInstance(CIPHER_ALGORITHM);
        generator.init(128, sr); // 192 and 256 bits may not be available
        SecretKey secretKey = generator.generateKey();

        return secretKey.getEncoded();
    }


    private static byte[] encrypt(byte[] raw, byte[] clear) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, CIPHER_ALGORITHM);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        SecretKeySpec secretKeySpec = new SecretKeySpec(raw, CIPHER_ALGORITHM);

        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        return cipher.doFinal(encrypted);
    }

    public static byte[] toByte(String hexString) {

        int len = hexString.length()/2;
        byte[] result = new byte[len];

        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {

        if (buf == null)
            return "";

        StringBuffer result = new StringBuffer(2 * buf.length);
        for (byte b : buf) {
            appendHex(result, b);
        }

        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
    }

}
