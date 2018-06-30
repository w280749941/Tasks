package com.heartiger.task_user.security;

public class SecurityConstants {
    public static final String SECRET = "SecretKeyToGenJWTs";
    public static final long EXPIRATION_TIME = 1_800_000; // 30 mins
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TASK_HEADER = "t-authorization";
    public static final String SIGN_UP_URL = "/api/users/new";

    public static final String RABBITMQ_USER_LOGIN_QUEUE = "userLogin";
    public static final String RABBITMQ_USER_TOKEN_REFRESH_QUEUE = "tokenRefresh";
    public static final String RABBITMQ_DELETE_USER_QUEUE = "deleteUser";
    public static final String RABBITMQ_DELETE_USER_COMPLETE_QUEUE= "userToDeleteComplete";
}
