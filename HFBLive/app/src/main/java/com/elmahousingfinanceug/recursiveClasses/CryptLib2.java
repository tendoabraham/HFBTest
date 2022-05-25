package com.elmahousingfinanceug.recursiveClasses;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
 * be used for encryption and decryption of string on iOS, Android and Windows
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

class CryptLib2 {
    /**
     * Encryption mode enumeration
     */
    private enum Mode {
        ENCRYPT, DECRYPT
    }
    // cipher to be used for encryption and decryption
    private Cipher _cx;

    // encryption key and initialization vector
    private byte[] _uce, _from;

    CryptLib2() throws NoSuchAlgorithmException, NoSuchPaddingException {
        // initialize the cipher with transformation AES/CBC/PKCS5Padding
        _cx = Cipher.getInstance("AES/CBC/PKCS5Padding");
        _uce = new byte[32]; //256 bit key space
        _from = new byte[16]; //128 bit IV
    }

    /**
     *
     * @param _x
     *            Text to be encrypted or decrypted
     * @param _uce
     *            Encryption key to used for encryption / decryption
     * @param _mode
     *            specify the mode encryption / decryption
     * @return encrypted or decrypted string based on the mode
     * @throws UnsupportedEncodingException **
     * @throws InvalidKeyException **
     * @throws InvalidAlgorithmParameterException **
     * @throws IllegalBlockSizeException **
     * @throws BadPaddingException **
     */
    private String flipFlop(String _x, String _uce, Mode _mode)
            throws UnsupportedEncodingException,
                InvalidKeyException,InvalidAlgorithmParameterException,
                 IllegalBlockSizeException,BadPaddingException {
        String _out = "";
        // output string
        //_uce = md5(_uce);
        //System.out.println("key="+_uce);
        int len = _uce.getBytes("UTF-8").length; // length of the key	provided

        if (_uce.getBytes("UTF-8").length > this._uce.length)
            len = this._uce.length;

        int from_len = "84jfkfndl3ybdfkf".getBytes("UTF-8").length;

        if("84jfkfndl3ybdfkf".getBytes("UTF-8").length > this._from.length)
            from_len = this._from.length;

        System.arraycopy(_uce.getBytes("UTF-8"), 0, this._uce, 0, len);
        System.arraycopy("84jfkfndl3ybdfkf".getBytes("UTF-8"), 0, this._from, 0, from_len);
        //KeyGenerator _keyGen = KeyGenerator.getInstance("AES");
        //_keyGen.init(128);
        SecretKeySpec uceSpec = new SecretKeySpec(this._uce, "AES");
                                    // Create a new SecretKeySpec
                                    // for the
                                    // specified key
                                    // data and
                                    // algorithm
                                    // name.
        IvParameterSpec fromSpec = new IvParameterSpec(this._from);
                                // Create a new
                                // IvParameterSpec
                                // instance with the
                                // bytes from the
                                // specified buffer
                                // iv used as
                                // initialization
                                // vector.

        // encryption
        if (_mode.equals(Mode.ENCRYPT)) {
            // Potentially insecure random numbers on Android 4.3 and older.
            // Read
            // https://android-developers.blogspot.com/2013/08/some-securerandom-thoughts.html
            // for more info.
            _cx.init(Cipher.ENCRYPT_MODE, uceSpec, fromSpec);// Initialize this cipher instance
            byte[] results = _cx.doFinal(_x.getBytes("UTF-8")); // Finish
                                        // multi-part
                                        // transformation
                                        // (encryption)
            _out = Base64.encodeToString(results, Base64.DEFAULT); // ciphertext
                                        // output
        }

        // decryption
        if (_mode.equals(Mode.DECRYPT)) {
            _cx.init(Cipher.DECRYPT_MODE, uceSpec, fromSpec);// Initialize this ipher instance

            byte[] decodedValue = Base64.decode(_x.getBytes(),
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
     * @param at input text whose SHA256 hash has to be computed
     * @return returns SHA256 hash of input text
     * @throws NoSuchAlgorithmException **
     * @throws UnsupportedEncodingException **
     */
    static String SHA256(String at) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        String resultStr;
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        md.update(at.getBytes("UTF-8"));
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
     * @param _x
     *            Plain text to be encrypted
     * @param _uce
     *            Encryption Key. You'll have to use the same key for decryption
     * @return returns encrypted (cipher) text
     * @throws InvalidKeyException **
     * @throws UnsupportedEncodingException **
     * @throws InvalidAlgorithmParameterException **
     * @throws IllegalBlockSizeException **
     * @throws BadPaddingException **
     */

    String flip(String _x, String _uce)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return flipFlop(_x, _uce, Mode.ENCRYPT);
    }

    /***
     * This function decrypts the encrypted text to plain text using the key
     * provided. You'll have to use the same key which you used during
     * encryption
     *
     * @param _x
     *            Encrypted/Cipher text to be decrypted
     * @param _uce
     *            Encryption key which you used during encryption
     * @return encrypted value
     * @throws InvalidKeyException **
     * @throws UnsupportedEncodingException **
     * @throws InvalidAlgorithmParameterException **
     * @throws IllegalBlockSizeException **
     * @throws BadPaddingException **
     */
    String flop(String _x, String _uce)
            throws InvalidKeyException, UnsupportedEncodingException,
            InvalidAlgorithmParameterException, IllegalBlockSizeException,
            BadPaddingException {
        return flipFlop(_x, _uce, Mode.DECRYPT);
    }
}
