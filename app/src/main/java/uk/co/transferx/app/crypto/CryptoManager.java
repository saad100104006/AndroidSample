package uk.co.transferx.app.crypto;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by sergey on 18/03/2018.
 */
@Singleton
public class CryptoManager {
    private final static String UTF_8 = "UTF-8";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private final static int ITERATION_COUNT = 2000;
    private final static int saltLength = 32;
    private static int KEY_LENGTH = 256;
    private static String DELIMITER = "]";
    private static final String PBKDF2_DERIVATION_ALGORITHM = "PBKDF2WithHmacSHA1";

    @Inject
    public CryptoManager() {
    }

    private SecretKey generateKey(String pin, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeySpec keySpec = new PBEKeySpec(pin.toCharArray(), salt,
                ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory
                .getInstance(PBKDF2_DERIVATION_ALGORITHM);
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        return new SecretKeySpec(keyBytes, "AES");
    }

    private byte[] getSalt() {
        byte[] salt;
        SecureRandom random = new SecureRandom();
        salt = new byte[saltLength];
        random.nextBytes(salt);
        return salt;
    }

    private byte[] generateIv(int length) {
        byte[] initialVector = new byte[length];
        SecureRandom random = new SecureRandom();
        random.nextBytes(initialVector);
        return initialVector;
    }

    public String getEncryptedCredential(String plaintext, String pin) {
        byte[] cipherText = null;
        byte[] initialVector = null;
        byte[] salt = getSalt();
        SecretKey key;
        try {
            key = generateKey(pin, salt);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            initialVector = generateIv(cipher.getBlockSize());
            IvParameterSpec ivParams = new IvParameterSpec(initialVector);
            cipher.init(Cipher.ENCRYPT_MODE, key, ivParams);
            cipherText = cipher.doFinal(plaintext.getBytes(UTF_8));
        } catch (UnsupportedEncodingException | InvalidKeyException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException
                | NoSuchAlgorithmException | BadPaddingException
                | NoSuchPaddingException | InvalidKeySpecException e) {
            Timber.e(CryptoManager.class.getSimpleName(), e);
        }
        return (cipherText == null || initialVector == null) ? null : String.format("%s%s%s%s%s", Base64.encodeToString(salt, Base64.NO_WRAP), DELIMITER,
                Base64.encodeToString(initialVector, Base64.NO_WRAP), DELIMITER, Base64.encodeToString(cipherText, Base64.NO_WRAP));
    }

    public String getDecryptCredential(String cipherText, String pin) {
        String[] fields = cipherText.split(DELIMITER);
        byte[] salt;
        byte[] initialVector;
        byte[] cipherBytes;
        String decryptedCredential = "";
        SecretKey key;
        try {
            if (fields.length != 3) {
                throw new IllegalArgumentException("Invalid encypted text format");
            }
            salt = Base64.decode(fields[0], Base64.NO_WRAP);
            initialVector = Base64.decode(fields[1], Base64.NO_WRAP);
            cipherBytes = Base64.decode(fields[2], Base64.NO_WRAP);
            key = generateKey(pin, salt);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec ivParams = new IvParameterSpec(initialVector);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParams);
            decryptedCredential = new String(cipher.doFinal(cipherBytes), UTF_8);
        } catch (IllegalArgumentException | BadPaddingException |
                InvalidKeySpecException | NoSuchAlgorithmException |
                NoSuchPaddingException | IllegalBlockSizeException |
                UnsupportedEncodingException | InvalidKeyException |
                InvalidAlgorithmParameterException e) {
            Timber.e(CryptoManager.class.getSimpleName(), e);
        }
        return decryptedCredential;
    }
}
