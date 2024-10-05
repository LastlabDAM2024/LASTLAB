package es.ifp.labsalut.seguridad;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;



/**
 * La clase CifradoAES proporciona funcionalidades para cifrar y descifrar
 * datos utilizando el algoritmo AES (Advanced Encryption Standard).
 *
 * Esta clase incluye métodos para generar una clave secreta a partir de una
 * semilla proporcionada, así como para cifrar y descifrar datos. Utiliza un
 * vector de inicialización (IV) aleatorio que se genera al instanciar la
 * clase, garantizando la seguridad del cifrado.
 *
 * Características principales:
 * - **Generación de Clave**: Se genera una clave secreta de 256 bits a partir
 *   del hash SHA-256 de una semilla proporcionada.
 * - **Cifrado**: Permite cifrar datos en texto plano, devolviendo el resultado
 *   en formato hexadecimal.
 * - **Descifrado**: Permite descifrar datos cifrados en formato hexadecimal y
 *   devuelve el texto original.
 *
 * Métodos destacados:
 * - `generarSecretKey(String semilla)`: Genera una clave secreta a partir de
 *   la semilla especificada.
 * - `encrypt(byte[] plaintext, SecretKey key)`: Cifra el texto plano y
 *   devuelve el texto cifrado en formato hexadecimal.
 * - `decrypt(String cipherText, SecretKey key)`: Descifra el texto cifrado y
 *   devuelve el texto original.
 *
 */

public class CifradoAES {
    private byte[] IV = new byte[32]; // Vector de inicialización para AES

    // Constructor de la clase
    public CifradoAES() {
        // Generar un vector de inicialización aleatorio
        SecureRandom random = new SecureRandom();
        random.nextBytes(IV);
    }

    // Método para generar una clave secreta a partir de una semilla
    public SecretKey generarSecretKey(String semilla) {
        SecretKey secreto = null;
        try {
            // Convertir la semilla a un array de bytes utilizando UTF-8
            byte[] seedBytes = semilla.getBytes(StandardCharsets.UTF_8);

            // Calcular el hash SHA-256 de los bytes de la semilla
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(seedBytes);

            // Utilizar solo los primeros 32 bytes para la clave AES (256 bits)
            byte[] aesKeyBytes = new byte[32]; // Tamaño de clave AES: 256 bits
            System.arraycopy(hashBytes, 0, aesKeyBytes, 0, aesKeyBytes.length);

            // Crear una instancia de SecretKeySpec para la clave AES
            secreto = new SecretKeySpec(aesKeyBytes, "AES");

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secreto;
    }

    // Método para convertir un array de bytes a una cadena hexadecimal
    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }

    // Método para convertir una cadena hexadecimal a un array de bytes
    private static byte[] hexToBytes(String hexString) {
        // Asegurar que la longitud de la cadena hexadecimal sea par
        if (hexString.length() % 2 != 0) {
            throw new IllegalArgumentException("La cadena hexadecimal debe tener una longitud par");
        }
        // Crear un array de bytes para almacenar el resultado
        int len = hexString.length();
        byte[] result = new byte[len / 2];

        // Convertir cada par de caracteres hexadecimales a bytes
        for (int i = 0; i < len; i += 2) {
            // Obtener el substring de dos caracteres hexadecimales
            String hex = hexString.substring(i, i + 2);
            // Convertir el substring a un byte y almacenarlo en el array resultante
            result[i / 2] = (byte) Integer.parseInt(hex, 16);
        }

        return result;
    }

    // Método para cifrar datos utilizando AES
    public String encrypt(byte[] plaintext, SecretKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(this.IV);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] cipherText = cipher.doFinal(plaintext);
        return bytesToHex(cipherText);
    }

    // Método para descifrar datos cifrados utilizando AES
    public String decrypt(String cipherText, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            SecretKeySpec keySpec = new SecretKeySpec(key.getEncoded(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(this.IV);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] cifrado = hexToBytes(cipherText);
            byte[] decryptedText = cipher.doFinal(cifrado);
            return new String(decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
