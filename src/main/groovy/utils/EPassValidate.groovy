package utils

import org.apache.commons.lang3.Validate


class EPassValidate extends Validate {
    private static final String EMAIL_REGEX = '^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$'

    static void isMD5Hash(String text, final String message, final Object... values) {
        notBlank(text, "password must be not null or whitespace")
        if (text ==~ '^[a-f0-9]{32}$')
            return
        throw new IllegalArgumentException(String.format(message, values))
    }

    static void isEmail(String text, final String message, final Object... values) {
        if (!(text ==~ EMAIL_REGEX)) {
            throw new IllegalArgumentException(String.format(message, values))
        }
    }
}
