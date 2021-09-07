package com.backend.service.utils;

import com.backend.model.User;
import org.json.JSONObject;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.rmi.ServerError;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

public class Security {

//    https://www.baeldung.com/java-aes-encryption-decryption
    public static SecretKey generateKeyFromPassword(String password, String salt)
        throws NoSuchAlgorithmException, InvalidKeySpecException {

            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
            SecretKey secret = new SecretKeySpec(factory.generateSecret(spec)
                    .getEncoded(), "AES");
            return secret;
    }

    //    https://www.baeldung.com/java-aes-encryption-decryption
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    //    https://www.baeldung.com/java-aes-encryption-decryption
    public static String encrypt(String algorithm, String input, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] cipherText = cipher.doFinal(input.getBytes());
        return Base64.getEncoder()
                .encodeToString(cipherText);
    }

    //    https://www.baeldung.com/java-aes-encryption-decryption
    public static String decrypt(String algorithm, String cipherText, SecretKey key,
                                 IvParameterSpec iv) throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] plainText = cipher.doFinal(Base64.getDecoder()
                .decode(cipherText));
        return new String(plainText);
    }

    private String JWTGenerator(String[] headerInfo, String payloadInfo, String signatureInfo) throws ServerError {

        JSONObject header = new JSONObject();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        JSONObject payload = new JSONObject();
        payload.put("sub", "User id");
        payload.put("exp", "86400");

        List<String> signatureToHash = new ArrayList<>();
        signatureToHash.add(Base64.getUrlEncoder().encodeToString(header.toString().getBytes()));
        signatureToHash.add(Base64.getUrlEncoder().encodeToString(payload.toString().getBytes()));

        String testSecret = "wan";

        signatureToHash.add(testSecret);

        String signature = hashStrings(signatureToHash);

        String base64Header = base64URLEncode(header);
        String base64Payload = base64URLEncode(payload);
//        String base64Payload = Base64.getEncoder().encodeToString(payload.toString().getBytes());

        String JWT = base64Header + "." + base64Payload + "." + signature;

        System.out.println(JWT);

        return null;
    }

    public static String base64URLEncode(JSONObject input) {
        String endodedInput = Base64.getUrlEncoder().encodeToString(input.toString().getBytes());
        return endodedInput;
    }

    public static  String hashPassword(String stringToHash, String salt) throws ServerError {
        final MessageDigest digest;
        try {
            String idPassword = stringToHash + salt;
            digest = MessageDigest.getInstance("SHA3-256");
            final byte[] hashbytes = digest.digest(
                    idPassword.getBytes(StandardCharsets.UTF_8)
            );

            String hashedPassword = bytesToHex(hashbytes);

//            byte[] decodedBytes = Base64.getDecoder().decode(hashbytes);
//            String hashedPassword = new String(decodedBytes);

            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServerError("Connot process password", null);
        }
    }

    public static  String hashStrings(List<String> listStringToHash) throws ServerError {
        final MessageDigest digest;
        try {
            String stringToHash = "";
            for (String tmp : listStringToHash) {
                stringToHash += tmp;
            }
            String idPassword = stringToHash;
            digest = MessageDigest.getInstance("SHA3-256");
            final byte[] hashbytes = digest.digest(
                    idPassword.getBytes(StandardCharsets.UTF_8)
            );

            String hashedPassword = bytesToHex(hashbytes);

//            byte[] decodedBytes = Base64.getDecoder().decode(hashbytes);
//            String hashedPassword = new String(decodedBytes);

            return hashedPassword;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new ServerError("Connot process password", null);
        }
    }

    // https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}
