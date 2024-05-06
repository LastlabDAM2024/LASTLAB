package es.ifp.labsalut.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class CifradoAES {
private byte[] IV = new byte[32];
    public CifradoAES(){
        SecureRandom random;
        random = new SecureRandom();
        random.nextBytes(IV);
    }

    public SecretKey generarSecretKey(String semilla) {
        SecretKey secreto=null;
        try {
            // Convertir el string a un array de bytes usando UTF-8
            byte[] seedBytes = semilla.getBytes(StandardCharsets.UTF_8);

            // Calcular el hash SHA-256 de los bytes de la semilla
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(seedBytes);

            // Utilizar solo los primeros 32 bytes para la clave AES (256 bits)
            byte[] aesKeyBytes = new byte[32];  // Tamaño de clave AES: 256 bits
            System.arraycopy(hashBytes, 0, aesKeyBytes, 0, aesKeyBytes.length);

            // Crear una instancia de SecretKeySpec para la clave AES
            secreto = new SecretKeySpec(aesKeyBytes, "AES");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secreto;
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    public byte[] encrypt(byte[] plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(this.IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(plaintext);
        return cipherText;
    }

    public  String decrypt(byte[] cipherText, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(this.IV);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decryptedText = cipher.doFinal(cipherText);
            return new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
