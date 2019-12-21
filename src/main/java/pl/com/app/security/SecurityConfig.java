package pl.com.app.security;

public interface SecurityConfig {
    int COOKIE_EXPIRATION_TIME = 300_000;
    String SESSION_PREFIX = "UserSessionId";
}
