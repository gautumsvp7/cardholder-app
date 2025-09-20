package com.example.cardholder.service;

import com.example.cardholder.exception.EncryptionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class EncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    @Value("${app.encryption.key}")
    private String encryptionKey;

    /**
     * Encrypts the given plaintext string using AES encryption
     * @param data the plaintext string to encrypt
     * @return Base64-encoded encrypted string
     * @throws EncryptionException if encryption fails
     */
    public String encrypt(String data) {
        try {
            if (data == null || data.isEmpty()) {
                throw new EncryptionException("Data to encrypt cannot be null or empty");
            }

            SecretKey secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedBytes);

        } catch (Exception e) {
            throw new EncryptionException("Failed to encrypt data: " + e.getMessage(), e);
        }
    }

    /**
     * Decrypts the given Base64-encoded encrypted string using AES decryption
     * @param encryptedData the Base64-encoded encrypted string to decrypt
     * @return the original plaintext string
     * @throws EncryptionException if decryption fails
     */
    public String decrypt(String encryptedData) {
        try {
            if (encryptedData == null || encryptedData.isEmpty()) {
                throw new EncryptionException("Encrypted data cannot be null or empty");
            }

            SecretKey secretKey = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new EncryptionException("Failed to decrypt data: " + e.getMessage(), e);
        }
    }
}
