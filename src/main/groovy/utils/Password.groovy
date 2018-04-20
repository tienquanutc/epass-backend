package utils

import org.apache.commons.codec.digest.DigestUtils

import static org.apache.commons.lang3.Validate.*

class Password {
    private static String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=])(?=\\\\S+\$).{8,}"

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

    static class Validate {
        static void isMD5Hash(String text, String message) {
            notBlank(text, "password must be not null or whitespace")
            if (text ==~ '^[a-f0-9]{32}$')
                return
            throw new RuntimeException(message)
        }
    }
}
