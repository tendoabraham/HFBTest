package com.elmahousingfinanceug_test.recursiveClasses;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*****************************************************************
 * CrossPlatform CryptLib
 *
 * <p>
 * This cross platform CryptLib uses AES 256 for encryption. This library can
 * be used for encryptoion and de-cryption of string on iOS, Android and Windows
 * platform.<br/>
 * Features: <br/>
 * 1. 256 bit AES encryption
 * 2. Random IV generation.
 * 3. Provision for SHA256 hashing of key.
 * </p>
 *
 * @since 1.0
 * @author navneet
 *****************************************************************/
public class CryptLib {
    /**
     * Encryption mode enumeration
     */
    private enum EncryptMode {
        ENCRYPT, DECRYPT;
    }
    // cipher to be used for encryption and decryption
    private Cipher _cx;
    // encryption key and initialization vector
    private byte[] _key, _iv;

    CryptLib() throws NoSuchAlgorithmException, NoSuchPaddingException {
        // initialize the cipher with transformation AES/CBC/PKCS5Padding
        _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
        _key = new byte[32]; //256 bit key space
        _iv = new byte[16]; //128 bit IV
    }

    /**
     *
     * @param _inputText
     *            Text to be encrypted or decrypted
     * @param _encryptionKey
     *            Encryption key to used for encryption / decryption
     * @param _mode
     *            specify the mode encryption / decryption
     * @return encrypted or decrypted string based on the mode
     * @throws UnsupportedEncodingException  //
     * @throws InvalidKeyException  //
     * @throws InvalidAlgorithmParameterException  //
     * @throws IllegalBlockSizeException  //
     * @throws BadPaddingException  //
     */
     private String encryptDecrypt(String _inputText, String _encryptionKey, EncryptMode _mode)
             throws UnsupportedEncodingException,InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        String _out = "";// output string

        int len = _encryptionKey.getBytes("UTF-8").length; // length of the key	provided

        if (_encryptionKey.getBytes("UTF-8").length > _key.length) len = _key.length;

        int ivlen = "84jfkfndl3ybdfkf".getBytes("UTF-8").length;

        if("84jfkfndl3ybdfkf".getBytes("UTF-8").length > _iv.length) ivlen = _iv.length;

        System.arraycopy(_encryptionKey.getBytes("UTF-8"), 0, _key, 0, len);
        System.arraycopy("84jfkfndl3ybdfkf".getBytes("UTF-8"), 0, _iv, 0, ivlen);

        SecretKeySpec keySpec = new SecretKeySpec(_key, "AES");
                                    // Create a new SecretKeySpec
                                    // for the
                                    // specified key
                                    // data and
                                    // algorithm
                                    // name.

        IvParameterSpec ivSpec = new IvParameterSpec(_iv);
                                // Create a new
                                // IvParameterSpec
                                // instance with the
                                // bytes from the
                                // specified buffer
                                // iv used as
                                // initialization
                                // vector.

        // encryption

        if (_mode.equals(EncryptMode.ENCRYPT)) {
            // Potentially insecure random numbers on Android 4.3 and older.
            // Read
            // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
            // for more info.
            _cx.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);// Initialize this cipher instance
            byte[] results = _cx.doFinal(_inputText.getBytes("UTF-8")); // Finish
                                        // multi-part
                                        // transformation
                                        // (encryption)
            _out = Base64.encodeToString(results, Base64.DEFAULT); // ciphertext
                                        // output
        }

        // decryption
        if (_mode.equals(EncryptMode.DECRYPT)) {
            _cx.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);// Initialize this ipher instance

            byte[] decodedValue = Base64.decode(_inputText.getBytes(),
                    Base64.DEFAULT);
            byte[] decryptedVal = _cx.doFinal(decodedValue); // Finish
                                    // multi-part
                                    // transformation
                                    // (decryption)
            _out = new String(decryptedVal);
        }

        return _out; // return encrypted/decrypted string
    }

    /***
     * This function computes the SHA256 hash of input string
     * @param text input text whose SHA256 hash has to be computed
     * @return returns SHA256 hash of input text
     * @throws NoSuchAlgorithmException ***
     * @throws UnsupportedEncodingException ***
     */
    static String SHA256(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String resultStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(text.getBytes("UTF-8"));
        byte[] digest = md.digest();

        StringBuilder result = new StringBuilder();
        for (byte b : digest) {
            result.append(String.format("%02x", b)); //convert to hex
        }
        //return result.toString();
        if(32 > result.toString().length()) {
            resultStr = result.toString();
        } else {
            resultStr = result.toString().substring(0, 32);
        }
        return resultStr;

    }

    /***
     * This function encrypts the plain text to cipher text using the key
     * provided. You'll have to use the same key for decryption
     *
     * @param _plainText
     *            Plain text to be encrypted
     * @param _key
     *            Encryption Key. You'll have to use the same key for decryption
     * @return returns encrypted (cipher) text
     * @throws InvalidKeyException //
     * @throws UnsupportedEncodingException //
     * @throws InvalidAlgorithmParameterException //
     * @throws IllegalBlockSizeException //
     * @throws BadPaddingException //
     */
    String encrypt(String _plainText, String _key)
            throws InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return encryptDecrypt(_plainText, _key, EncryptMode.ENCRYPT);
    }

    /***
     * This funtion decrypts the encrypted text to plain text using the key
     * provided. You'll have to use the same key which you used during
     * encryprtion
     *
     * @param _encryptedText
     *            Encrypted/Cipher text to be decrypted
     * @param _key
     *            Encryption key which you used during encryption
     * @return encrypted value
     * @throws InvalidKeyException //
     * @throws UnsupportedEncodingException //
     * @throws InvalidAlgorithmParameterException //
     * @throws IllegalBlockSizeException //
     * @throws BadPaddingException //
     */
    String decrypt(String _encryptedText, String _key)
            throws InvalidKeyException, UnsupportedEncodingException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        return encryptDecrypt(_encryptedText, _key, EncryptMode.DECRYPT);
    }

    /**
     * this function generates random string for given length
     * @param length
     * 				Desired length
     * * @return
     */
//    public static String generateRandomIV(int length) {
//        SecureRandom ranGen = new SecureRandom();
//        byte[] aesKey = new byte[16];
//        ranGen.nextBytes(aesKey);
//        StringBuilder result = new StringBuilder();
//        for (byte b : aesKey) {
//            result.append(String.format("%02x", b)); //convert to hex
//        }
//        if(length> result.toString().length()) {
//            return result.toString();
//        } else {
//            return result.toString().substring(0, length);
//        }
//    }
}
