package com.swift.akc.network;

public final class ApiEndpoint {

    private ApiEndpoint() {}

    private static final String BASE_URL = "http://10.0.2.2:8080";

    public static final String LOGIN_API = BASE_URL + "/login/userValidation";

}