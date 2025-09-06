package com.idb.microservicedemo.library.service.auth;

import com.idb.microservicedemo.library.common.Result;
import com.idb.microservicedemo.library.dto.auth.LoginResponse;

public interface AuthService {
    Result<LoginResponse> login(String emailOrUsername, String password);
}
