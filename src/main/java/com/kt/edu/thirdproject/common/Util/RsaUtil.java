package com.kt.edu.thirdproject.common.Util;

import com.kt.edu.thirdproject.common.exception.CustomEduException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


@Slf4j
@Component
public class RsaUtil {

    private static final String base64PublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjP+vQLmJ0twpINzJrCCVVaegylQzYk4dySHjzguBApA6JwLzxlZLRh9zqwcwq99yKGXRwM9dLXrY5jBxiXqrFsKY5DWLmCqR2b+/lg57EFURxlUE4jd8SsF782pIpLRpRkpkKXqLyWThR6wDu9WwCbWFV/V39ISe2d7FLnEy4LHEAyEdhbjYphLfvEz1vPDTHB5nU2AqY3kY5DTbZ2vCNoR2xwAnZfynpCuCXzGhbqFURL/F2OT3WyFmz5Wmpi7NzKsACfYuGIkXos1FrsCeaelLNkALqEQ1FEqZ//R44l+aN+wMD4AeEXypIswPkKcPBoLuCHRVFJFemlOZuJfQxwIDAQAB";
    private static final String base64PrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCM/69AuYnS3Ckg3MmsIJVVp6DKVDNiTh3JIePOC4ECkDonAvPGVktGH3OrBzCr33IoZdHAz10tetjmMHGJeqsWwpjkNYuYKpHZv7+WDnsQVRHGVQTiN3xKwXvzakiktGlGSmQpeovJZOFHrAO71bAJtYVX9Xf0hJ7Z3sUucTLgscQDIR2FuNimEt+8TPW88NMcHmdTYCpjeRjkNNtna8I2hHbHACdl/KekK4JfMaFuoVREv8XY5PdbIWbPlaamLs3MqwAJ9i4YiReizUWuwJ5p6Us2QAuoRDUUSpn/9HjiX5o37AwPgB4RfKkizA+Qpw8Ggu4IdFUUkV6aU5m4l9DHAgMBAAECggEAB4ylII0OoRiipxzLO4kfFc/83vh1K91M4PkMjnq1NKLuSipxTPxP2XhTpCOlan5zSVByU6WGsqmyNVkAq0DDKlvO/eEUEViuKH+IzYsG8c/sPLdOpKId6I7FiTn2m8MBI/9Vr5bdJzJckY7XQFZnr41lj1kYTCJRsw6ndh4fgEJQA0NMm4GavwJb/1ViCNx1dXGB6cOYUESVOb5Ap5NrF1/xWlJWkVEmIqnnCVSTT1g7q39F+kLHSTteRD4FMwbZHaOOwLCioCCIzfSlVQ9W+w6/XTenT/z2KYnLgB3o+eQZgtqXPPkmpUd/S3irnkhWH1FghhDnZ3pPsT5uh7WNsQKBgQDHLT1Zihfs/fbkStfDkoB8O1WE74zg/kGLI28HBtACW0P8gMHj7f/PItzDdrckdNpzE4iBVGgOr7QkWkHIg93rjVplXogLx3Hl/CC+TCX14U9vsXIeNVkh1JwTCsipNDbpjd3mfGQiJfEhM2vD7T5WiAokwv8bcmOFTBoUsDKryQKBgQC1OXNshhUMAQi4tidmF5STJXAlDR4PZ+yFDCxy2UVlCtDy7E/alY4jvFNdIY7LxfBj3/Knsp0Kz9tQDnCUciAjpxTAlpwSphJiRsci8n7usAREuQoBBuyrUvHvXPTegTaFsnALx8EuvQ/nJNraj6Q4/lNx1q496PdpbLX/cQHADwKBgQCD1ESj7AvT93AfQA45StEx6M+8shULoh3dqvFEtFSfCqsgCFTMuO00uz1lPkEPpywjOI2EFErfVZok6Xxa7DTJQIDRUVU4fqS7dTpy2dHSQXOjWM5QgjzhcGhxDhUsMPwbb6Osdy6Lj5NexzsPrgoxmc8k78+L6hVG18z3XnvbSQKBgFArWiRoLGdjDbio3EUNqWxu422AoAuwrbEt8XvdIhXQ8x6D68+G2Zp1dWt9rO34Yp/MMkx9d3uQ+DQyLxt8YTtkbonEFscpMHjJVs7e98gflcPRF9vcdFZWGzEIrkrAXp5GkcqkU5GZka9vXmHRqZSM7d0uW2aH6Ot6bAjUtTGBAoGBAL/gQ5Xb2DJds5Qkqqh/1LvPmYBNuV0xtH/9st8t1f8rvP+h0ayAWjoUMAgxGBwzdEcSF1KMf6mFXgWY3BDbtrym39wOI1NbXbeO6sey482vA4tViD5+FBNLXO06QScuQYD+RkX886U9P6NBPWnc/FeH6q5hn94dDtj9d8X5NB0m";

    public static String encryptRSA(String plainText, PublicKey publicKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytePlain = cipher.doFinal(plainText.getBytes());

        String encrypted = Base64.getEncoder().encodeToString(bytePlain);
        return encrypted;

    }

    public static String decryptRSA(String encrypted, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA");
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        String decrypted = new String(bytePlain, StandardCharsets.UTF_8);
        return decrypted;
    }

    public static PublicKey getPublicKeyFromBase64Encrypted(String base64PublicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] decodedBase64PubKey = Base64.getDecoder().decode(base64PublicKey);
        return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
    }


    public static PrivateKey getPrivateKeyFromBase64Encrypted(String base64PrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {

        byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(base64PrivateKey);
        return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));

    }

   /* public static PrivateKey getPrivateKeyPkcs1FromBase64Encrypted(String base64PrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, IOException {

        DerInputStream derReader = new DerInputStream(Base64.getDecoder().decode(base64PrivateKey));
        DerValue[] seq = derReader.getSequence(0);

        RSAPrivateCrtKeySpec keySpec =
                new RSAPrivateCrtKeySpec(seq[1].getBigInteger(), seq[2].getBigInteger(), seq[3].getBigInteger(), seq[4].getBigInteger(), seq[5].getBigInteger(), seq[6].getBigInteger(), seq[7].getBigInteger(), seq[8].getBigInteger());

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }*/

    public static String passwordDescryptRSA(String encrypted) {

        try {
            //PrivateKey privateKey = getPrivateKeyPkcs1FromBase64Encrypted(base64PrivateKey);
            PrivateKey privateKey = getPrivateKeyFromBase64Encrypted(base64PrivateKey);
            return decryptRSA(encrypted, privateKey);

        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            log.error("알 수 없는 암호화 알고리즘입니다. 암호화 하지 않습니다.", e);
            throw new CustomEduException("알 수 없는 암호화 알고리즘입니다. 암호화 하지 않습니다.", e);
        } catch (InvalidKeyException | InvalidKeySpecException e) {
            log.error("Key 초기화 오류 입니다. 암호화 하지 않습니다.", e);
            throw new CustomEduException("Key 초기화 오류 입니다. 암호화 하지 않습니다.", e);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            log.error("암호화 오류 입니다. 암호화 하지 않습니다.", e);
            throw new CustomEduException("암호화 오류 입니다. 암호화 하지 않습니다.", e);
        }
    }
}