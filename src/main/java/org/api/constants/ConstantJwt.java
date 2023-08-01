package org.api.constants;

public class ConstantJwt {
    public static final Integer ACCESS_TOKEN_VALIDITY_SECONDS = 60 * 60 * 24;
    public static final Integer PASSWORD_RESET_EXPIRATION_TIME = 60 * 60 * 24;
    public static final String SIGNING_KEY = "api.authorization.token.signing.key";
    public static final String TOKEN_PREFIX = "api.authorization.type";
    public static final String HEADER_STRING = "api.authorization.name";
}
