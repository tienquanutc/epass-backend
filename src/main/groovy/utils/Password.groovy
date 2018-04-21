package utils

import org.apache.commons.codec.digest.DigestUtils

import static org.apache.commons.lang3.Validate.*

class Password {

    public static final String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String lower = upper.toLowerCase(Locale.ROOT);
    public static final String digits = "0123456789";


    static String hash(String rawPassword) {
        return DigestUtils.md5Hex(rawPassword).toLowerCase();
    }

    static String encrypt(String text, String secretKey) {
        return new String(Base64.encoder.encode((text + secretKey).bytes))
    }

    static String decrypt(String encryptedText, String secretKey) {
        String firstDecode = new String(Base64.decoder.decode(encryptedText.bytes))
        return firstDecode.substring(0, firstDecode.length() - secretKey.length())
    }

    static boolean compare(String rawPassword, String encryptPassword, String secretKey) {
        return decrypt(encryptPassword, secretKey).equals(rawPassword)
    }

    static String generateRandomPassword() {
        return UUID.randomUUID().toString().replaceAll('-', '')
    }
}
