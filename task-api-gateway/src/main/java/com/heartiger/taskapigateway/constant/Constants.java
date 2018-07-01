package com.heartiger.taskapigateway.constant;

public class Constants {

    public static final String USER_TOKEN_TEMPLATE = "token_%s";

    public static final String REQUEST_HEADER_TOKEN = "t-authorization";

    public static final String LOGOUT_URI = "/user/logout";

    public static final String TOKEN_PREFIX = "Bearer ";

    public static final String RABBITMQ_USER_LOGIN_QUEUE = "userLogin";
    public static final String RABBITMQ_USER_TOKEN_REFRESH_QUEUE = "tokenRefresh";
    public static final String RABBITMQ_DELETE_USER_QUEUE = "deleteUser";
    public static final String RABBITMQ_USER_NAME_FROM_TOKEN_QUEUE = "tokenToUserName";

}
